package org.freda.thrones.framework.remote.transport;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.handler.ChannelChainHandlerAdapter;
import org.freda.thrones.framework.remote.handler.ChannelChainHandlerDispatcher;
import org.freda.thrones.framework.remote.client.Client;
import org.freda.thrones.framework.remote.server.Server;

import java.util.Objects;

/**
 * Create on 2018/9/5 14:34
 */
public class TransporterKernel {


    public static Server bind(URL url, ChannelChainHandler... handlers) throws LinkingException {
        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(handlers.length > 0, "handlers can not be null");

        ChannelChainHandler handler;
        if (handlers.length == 1) {
            handler = handlers[0];
        } else {
            handler = new ChannelChainHandlerDispatcher(handlers);
        }
        return new NettyTransporter().bind(url, handler);
    }

    public static Client connect(String url, ChannelChainHandler... handler) throws LinkingException {
        return connect(URL.valueOf(url), handler);
    }

    public static Client connect(URL url, ChannelChainHandler... handlers) throws LinkingException {
        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");

        ChannelChainHandler handler;
        if (handlers.length == 0) {
            handler = new ChannelChainHandlerAdapter();
        } else if (handlers.length == 1) {
            handler = handlers[0];
        } else {
            handler = new ChannelChainHandlerDispatcher(handlers);
        }
        return new NettyTransporter().connect(url, handler);
    }


}
