package org.freda.thrones.framework.remote.future;

import org.freda.thrones.framework.exceptions.LinkingException;

import java.util.concurrent.TimeUnit;

/**
 * CommonFuture
 */
public interface CommonFuture {

    /**
     * done or not
     */
    boolean isDone();

    /**
     * get response
     */
    Object get() throws LinkingException;

    /**
     * get response in timeout
     */
    Object get(long timeout, TimeUnit unit) throws LinkingException;

    /**
     * do cancel
     */
    boolean cancel();

    /**
     * cancelled or not
     */
    boolean isCancelled();

    /**
     * set callback
     */
    void setCallBack(FutureCallBack callBack);

}
