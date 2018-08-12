package org.freda.thrones.framework.remote.future;

import java.util.concurrent.Future;

/**
 * SyncFuture
 * @param <T>
 */
public interface SyncFuture<T> extends Future<T> {

    /**
     * set callback
     */
    void setCallBack(FutureCallBack callBack);

}
