package org.freda.thrones.framework.concurrent;

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
