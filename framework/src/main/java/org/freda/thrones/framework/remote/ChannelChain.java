package org.freda.thrones.framework.remote;

import java.net.InetSocketAddress;

/**
 * A communication between client and server
 */
public interface ChannelChain extends NodePoint {

    /**
     * get local address
     */
    InetSocketAddress getLocalAddress();

    /**
     * connect or not
     */
    boolean isConnected();
}
