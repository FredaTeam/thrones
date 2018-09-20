package org.freda.thrones.framework.manager;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/3 15:35
 * <p>
 * for service export and invoke
 */
public interface Kernel {

    /**
     * export by provider
     */
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    /**
     * invoke by consumer
     */
    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    /**
     * stop all service export and invoke & release all occupied resources
     */
    void destroy();
}
