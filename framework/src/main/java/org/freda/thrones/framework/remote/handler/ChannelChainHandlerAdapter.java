package org.freda.thrones.framework.remote.handler;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;

/**
 * Create on 2018/9/5 14:43
 */
public class ChannelChainHandlerAdapter implements ChannelChainHandler {

    @Override
    public void onConnected(ChannelChain channelChain) throws LinkingException {

    }

    @Override
    public void onDisConnected(ChannelChain channelChain) throws LinkingException {

    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {

    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) throws LinkingException {

    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) throws LinkingException {

    }
}
