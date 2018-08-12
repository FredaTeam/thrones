package org.freda.thrones.framework.remote;

/**
 * the action trigger of channel chain
 */
public interface ChanneChainlHandler {

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
    void onReceived(ChannelChain channelChain);

    /**
     * the moment of sending message
     */
    void onSent(ChannelChain channelChain, Object message);

    /**
     * the moment of throw exception
     */
    void onError(ChannelChain channelChain, Throwable throwable);
}
