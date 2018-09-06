package org.freda.thrones.framework.manager.support;

import com.google.common.collect.Maps;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.export.ThronesExporter;
import org.freda.thrones.framework.manager.invoke.Invoker;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.exchange.AbstractExchangeHandler;
import org.freda.thrones.framework.remote.exchange.ExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeHandler;
import org.freda.thrones.framework.remote.server.ExchangeServer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Create on 2018/9/3 15:51
 */
public class ThronesKernel extends AbstractKernel {

    // <host:port,Exchanger>
    private final Map<String, ExchangeServer> serverMap = Maps.newConcurrentMap();

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

        ExchangeServer server = null;

        return server;
    }


    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public void destroy() {

    }
}
