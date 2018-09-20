package org.freda.thrones.framework.manager.invoke;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;

/**
 * Create on 2018/9/3 15:36
 */
public interface Invoker<T> {

    /**
     * get service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     */
    Result invoke(Invocation invocation) throws RpcException;

    /**
     * get url.
     */
    URL getUrl();

    /**
     * is available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();

}
