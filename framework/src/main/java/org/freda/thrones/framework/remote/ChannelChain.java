package org.freda.thrones.framework.remote;

import java.net.InetSocketAddress;

/**
 * A communication between client and server
 */
public interface ChannelChain {

    /**
     * get remote address
     */
    InetSocketAddress getRemoteAddress();

    /**
     * get local address
     */
    InetSocketAddress getLocalAddress();

    /**
     * connect or not
     */
    boolean isConnected();
}
