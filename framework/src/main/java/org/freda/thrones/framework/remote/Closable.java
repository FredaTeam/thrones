package org.freda.thrones.framework.remote;

public interface Closable {
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
}
