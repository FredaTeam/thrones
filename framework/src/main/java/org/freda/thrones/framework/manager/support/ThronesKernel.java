package org.freda.thrones.framework.manager.support;

import com.google.common.collect.Maps;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.export.ThronesExporter;
import org.freda.thrones.framework.manager.invoke.Invoker;
import org.freda.thrones.framework.manager.invoke.ThronesInvoker;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.client.ExchangeClient;
import org.freda.thrones.framework.remote.exchange.AbstractExchangeHandler;
import org.freda.thrones.framework.remote.exchange.ExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeHandler;
import org.freda.thrones.framework.remote.exchange.ExchangerKernel;
import org.freda.thrones.framework.remote.server.ExchangeServer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * Create on 2018/9/3 15:51
 */
public class ThronesKernel extends AbstractKernel {

    // <host:port,Exchanger>
    private final Map<String, ExchangeServer> serverMap = Maps.newConcurrentMap();

    private final Map<String, ExchangeClient> clientMap = Maps.newConcurrentMap();

    private final ConcurrentMap<String, Object> locks = Maps.newConcurrentMap();

    private ExchangeHandler thronesHandler = new AbstractExchangeHandler() {

        @Override
        public CompletableFuture<Object> reply(ExchangeChannelChain channel, Object request) throws LinkingException {
            return super.reply(channel, request);
        }

        @Override
        public void onConnected(ChannelChain channelChain) {

        }

        @Override
        public void onDisConnected(ChannelChain channelChain) {

        }

        @Override
        public void onReceived(ChannelChain channelChain, Object message) {

        }

        @Override
        public void onSent(ChannelChain channelChain, Object message) {

        }

        @Override
        public void onError(ChannelChain channelChain, Throwable throwable) {

        }
    };

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        URL url = invoker.getUrl();
        String serviceKey = serviceKey(url);

        ThronesExporter<T> thronesExporter = new ThronesExporter<T>(invoker, serviceKey, exporterMap);
        exporterMap.put(serviceKey, thronesExporter);

        connectToServer(url);
        return thronesExporter;
    }

    private void connectToServer(URL url) {

        // host:port
        String address = url.getAddress();

        boolean isServer = url.getParam(Constants.PARAMETER.IS_SERVER_KEY, true);
        if (isServer) {
            ExchangeServer server = serverMap.get(address);
            if (Objects.isNull(server)) {
                server = serverMap.get(address);
                if (Objects.isNull(server)) {
                    serverMap.put(address, createServer(url));
                }
            } else {
                server.reset(url);
            }
        }
    }

    private ExchangeServer createServer(URL url) {
        try {
            return ExchangerKernel.bind(url, thronesHandler);
        } catch (LinkingException e) {
            throw new RpcException("Fail to start server(url: " + url + ") " + e.getMessage(), e);
        }
    }


    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        ThronesInvoker<T> invoker = new ThronesInvoker<T>(type, url, getClients(url), invokers);
        invokers.add(invoker);
        return invoker;
    }

    private ExchangeClient[] getClients(URL url) {
        String key = url.getAddress();
        ExchangeClient client = clientMap.get(key);
        if (Objects.nonNull(client)) {
            if (!client.closed()) {
                return new ExchangeClient[]{client};
            } else {
                clientMap.remove(key);
            }
        }
        locks.putIfAbsent(key, new Object());
        synchronized (locks.get(key)) {
            client = initClient(url);
            clientMap.put(key, client);
            locks.remove(key);
        }
        return new ExchangeClient[]{client};
    }

    private ExchangeClient initClient(URL url) {
        try {
            return ExchangerKernel.connect(url, thronesHandler);
        } catch (LinkingException e) {
            throw new RpcException("Fail to create remoting client for service(" + url + "): " + e.getMessage(), e);
        }

    }

    @Override
    public void destroy() {
        for (String key : serverMap.keySet()) {
            ExchangeServer server = serverMap.remove(key);
            if (Objects.nonNull(server)) {
                server.close();
            }
        }
        for (String key : clientMap.keySet()) {
            ExchangeClient client = clientMap.remove(key);
            if (Objects.nonNull(client)) {
                client.close();
            }
        }
    }
}
