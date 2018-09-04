package org.freda.thrones.framework.manager.export;

import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/3 15:36
 */
public interface Exporter<T> {


    /**
     * get invoker.
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport. (destroy)
     */
    void unexport();

}
