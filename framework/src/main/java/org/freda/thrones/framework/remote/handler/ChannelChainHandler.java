package org.freda.thrones.framework.remote.handler;

import org.freda.thrones.framework.remote.ChannelChain;

/**
 * the action trigger of channel chain
 */
public interface ChannelChainHandler {

    /**
     * the moment of connetion
     */
    void onConnected(ChannelChain channelChain);

    /**
     * the moment of disconnection
     */
    void onDisConnected(ChannelChain channelChain);

    /**
     * the moment of receiving
     */
    void onReceived(ChannelChain channelChain, Object message);

    /**
     * the moment of sending message
     */
    void onSent(ChannelChain channelChain, Object message);

    /**
     * the moment of throw exception
     */
    void onError(ChannelChain channelChain, Throwable throwable);
}
