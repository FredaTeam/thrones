package org.freda.thrones.framework.manager.proxy;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;
import org.freda.thrones.framework.manager.invoke.AbstractProxyInvoker;
import org.freda.thrones.framework.manager.invoke.Invoker;

import java.lang.reflect.Method;
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

        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }
}
