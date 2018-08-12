package org.freda.thrones.framework.remote;


import java.net.InetSocketAddress;
import java.net.URL;

/**
 * points of a linking channel
 * <p>
 * list info and action from the point instead of a channel
 */
public interface NodePoint {

    /**
     * get url of this point
     */
    URL getUrl();

    /**
     * close communication
     */
    void close();

    /**
     * close communication after time of timeout
     */
    void close(int timeout);

    /**
     * during closing
     */
    void closing();

    /**
     * close or not
     */
    boolean closed();

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
