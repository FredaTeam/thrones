package org.freda.thrones.framework.manager.proxy;

import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/4 22:22
 */
public abstract class AbstractProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        Class<?>[] interfaces = new Class<?>[]{invoker.getInterface()};
        return getProxy(invoker, interfaces);
    }

    protected abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces);

}
