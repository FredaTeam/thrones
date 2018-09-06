package org.freda.thrones.framework.remote;


import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;

import java.net.InetSocketAddress;

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
    void send(Object message) throws LinkingException;

    /**
     * sent or not
     */
    void send(Object message, boolean sent) throws LinkingException;

    /**
     * reset link connection
     */
    void reset(URL url);
}
