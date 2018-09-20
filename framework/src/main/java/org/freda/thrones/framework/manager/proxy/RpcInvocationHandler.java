package org.freda.thrones.framework.manager.proxy;

import org.freda.thrones.framework.manager.RpcInvocation;
import org.freda.thrones.framework.manager.invoke.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Create on 2018/9/4 22:28
 */
public class RpcInvocationHandler implements InvocationHandler {

    private final Invoker<?> invoker;

    public RpcInvocationHandler(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }
        RpcInvocation invocation = new RpcInvocation(method, args);

        return invoker.invoke(invocation).recreate();
    }
}
