package org.freda.thrones.framework.manager.invoke;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;
import org.freda.thrones.framework.manager.RpcResult;

import java.util.Objects;

/**
 * Create on 2018/9/19 21:52
 */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private T proxy;

    private Class<T> type;

    private URL url;

    public AbstractProxyInvoker(T proxy, Class<T> type, URL url) {
        Preconditions.checkArgument(Objects.nonNull(proxy), "proxy can not be null");
        Preconditions.checkArgument(Objects.nonNull(type), "interface can not be null");
        Preconditions.checkArgument(type.isInstance(proxy), "type is not instance of proxy");

        this.proxy = proxy;
        this.type = type;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        try {
            Object result = doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
            return new RpcResult(result);
        } catch (Throwable t) {
            throw new RpcException("Failed to invoke remote proxy method " + invocation.getMethodName() + " to " + getUrl() + ", cause: " + t.getMessage(), t);
        }
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void destroy() {

    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;
}
