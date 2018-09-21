package org.freda.thrones.framework.manager.support;

import com.google.common.collect.Maps;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.export.ThronesExporter;
import org.freda.thrones.framework.manager.invoke.Invoker;
import org.freda.thrones.framework.manager.invoke.ThronesInvoker;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.client.ExechangeClient;
import org.freda.thrones.framework.remote.exechange.AbstractExechangeHandler;
import org.freda.thrones.framework.remote.exechange.ExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeHandler;
import org.freda.thrones.framework.remote.exechange.ExechangerKernel;
import org.freda.thrones.framework.remote.server.ExechangeServer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * Create on 2018/9/3 15:51
 */
public class ThronesKernel extends AbstractKernel {

    // <host:port,Exechanger>
    private final Map<String, ExechangeServer> serverMap = Maps.newConcurrentMap();

    private final Map<String, ExechangeClient> clientMap = Maps.newConcurrentMap();

    private final ConcurrentMap<String, Object> locks = Maps.newConcurrentMap();

    private ExechangeHandler thronesHandler = new AbstractExechangeHandler() {

        @Override
        public CompletableFuture<Object> reply(ExechangeChannelChain channelChain, Object request) throws LinkingException {
            if (request instanceof Invocation) {
                Invocation inv = (Invocation) request;
                Invoker<?> invoker = getInvoker(channelChain, inv);
                Result result = invoker.invoke(inv);
                return CompletableFuture.completedFuture(result);
            }
            throw new LinkingException(channelChain, "Unsupported request: "
                    + (request == null ? null : (request.getClass().getName() + ": " + request))
                    + ", channel: consumer: " + channelChain.getRemoteAddress() + " --> provider: " + channelChain.getLocalAddress());
        }

        @Override
        public void onConnected(ChannelChain channelChain) {

        }

        @Override
        public void onDisConnected(ChannelChain channelChain) {

        }

        @Override
        public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {
            if (message instanceof Invocation) {
                reply((ExechangeChannelChain) channelChain, message);
            }
        }

        @Override
        public void onSent(ChannelChain channelChain, Object message) {

        }

        @Override
        public void onError(ChannelChain channelChain, Throwable throwable) {

        }
    };

    Invoker<?> getInvoker(ChannelChain channelChain, Invocation inv) throws LinkingException {
        String serviceKey = serviceKey(channelChain.getUrl());

        ThronesExporter<?> exporter = (ThronesExporter<?>) exporterMap.get(serviceKey);

        if (exporter == null) {
            throw new LinkingException(channelChain, "Not found exported service: " + serviceKey + " in " + exporterMap.keySet() + ", may be version or group mismatch " + ", channel: consumer: " + channelChain.getRemoteAddress() + " --> provider: " + channelChain.getLocalAddress() + ", message:" + inv);
        }
        return exporter.getInvoker();
    }

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
            ExechangeServer server = serverMap.get(address);
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

    private ExechangeServer createServer(URL url) {
        try {
            return ExechangerKernel.bind(url, thronesHandler);
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

    private ExechangeClient[] getClients(URL url) {
        String key = url.getAddress();
        ExechangeClient client = clientMap.get(key);
        if (Objects.nonNull(client)) {
            if (!client.closed()) {
                return new ExechangeClient[]{client};
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
        return new ExechangeClient[]{client};
    }

    private ExechangeClient initClient(URL url) {
        try {
            return ExechangerKernel.connect(url, thronesHandler);
        } catch (LinkingException e) {
            throw new RpcException("Fail to create remoting client for service(" + url + "): " + e.getMessage(), e);
        }

    }

    @Override
    public void destroy() {
        for (String key : serverMap.keySet()) {
            ExechangeServer server = serverMap.remove(key);
            if (Objects.nonNull(server)) {
                server.close();
            }
        }
        for (String key : clientMap.keySet()) {
            ExechangeClient client = clientMap.remove(key);
            if (Objects.nonNull(client)) {
                client.close();
            }
        }
    }
}
