package org.freda.thrones.framework.manager.proxy;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/3 18:34
 */
public interface ProxyFactory {

    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;
}
