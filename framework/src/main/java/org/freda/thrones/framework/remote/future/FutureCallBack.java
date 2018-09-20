package org.freda.thrones.framework.remote.future;

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
