package org.freda.thrones.framework.remote.handler;

import org.freda.thrones.framework.common.URL;

/**
 * Create on 2018/9/19 16:49
 */
public class HandlerKernel {

    private static HandlerKernel INSTANCE = new HandlerKernel();

    public static ChannelChainHandler wrap(ChannelChainHandler handler, URL url) {
        return HandlerKernel.getInstance().wrapInternal(handler, url);
    }

    public static HandlerKernel getInstance() {
        return INSTANCE;
    }

    protected ChannelChainHandler wrapInternal(ChannelChainHandler handler, URL url) {
        return new MessageHandler(handler, url);
    }
}
