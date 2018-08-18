package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.client.Client;

/**
 * Create on 2018/8/18 14:23
 */
public abstract class AbstractClient implements Client {


    /**
     * open client.
     */
    protected abstract void doOen() throws Throwable;

    /**
     * close client.
     */
    protected abstract void doClose() throws Throwable;

    /**
     * connect to the server.
     */
    protected abstract void doConnect() throws Throwable;

    /**
     * disConnect to the server.
     */
    protected abstract void doDisConnect() throws Throwable;

    /**
     * get the current connecting channelChain
     */
    protected abstract ChannelChain getChannelChain();
}
