package org.freda.thrones.framework.manager.proxy;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.invoke.Invoker;

import java.lang.reflect.Proxy;

/**
 * Create on 2018/9/4 22:17
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                interfaces,
                new RpcInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        return null;
    }
}
