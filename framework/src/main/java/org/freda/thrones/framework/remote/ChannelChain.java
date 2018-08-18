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

    // ============= additional info =============

    /**
     * has attribute.
     */
    boolean hasAttribute(String key);

    /**
     * get attribute.
     */
    Object getAttribute(String key);

    /**
     * set attribute.
     */
    void setAttribute(String key, Object value);

    /**
     * remove attribute.
     */
    void removeAttribute(String key);
}
