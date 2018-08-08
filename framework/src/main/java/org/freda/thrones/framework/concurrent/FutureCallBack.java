package org.freda.thrones.framework.concurrent;

/**
 * callback for future
 */
public interface FutureCallBack {

    /**
     * execute success
     */
    void success(Object response);

    /**
     * caught exception where execute error
     */
    void failure(Throwable throwable);
}
