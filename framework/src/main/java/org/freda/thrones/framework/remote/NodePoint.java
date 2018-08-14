package org.freda.thrones.framework.remote;


import java.net.InetSocketAddress;
import java.net.URL;

/**
 * points of a linking channel
 * <p>
 * list info and action from the point instead of a channel
 */
public interface NodePoint extends Closable {

    /**
     * get remote address
     */
    InetSocketAddress getRemoteAddress();

    /**
     * get url of this point
     */
    URL getUrl();

    /**
     * send message
     */
    void send(Object message);

    /**
     * sent or not
     */
    void send(Object message, boolean sent);

    /**
     * reset link connection
     */
    void reset(URL url);
}
