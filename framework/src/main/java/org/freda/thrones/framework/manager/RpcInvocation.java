package org.freda.thrones.framework.manager;

import com.google.common.collect.Maps;
import org.freda.thrones.framework.manager.invoke.Invoker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Create on 2018/9/4 09:52
 */
public class RpcInvocation implements Invocation, Serializable {

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, String> attachments;

    private transient Invoker<?> invoker;

    public RpcInvocation() {
    }

    public RpcInvocation(Invocation invocation, Invoker<?> invoker) {
        this(invocation.getMethodName(),
                invocation.getParameterTypes(),
                invocation.getArguments(),
                invocation.getAttachments(),
                invocation.getInvoker()
        );
    }

    public RpcInvocation(Invocation invocation) {
        this(invocation.getMethodName(), invocation.getParameterTypes(),
                invocation.getArguments(), invocation.getAttachments(), invocation.getInvoker());
    }

    public RpcInvocation(Method method, Object[] arguments) {
        this(method.getName(), method.getParameterTypes(), arguments, null, null);
    }

    public RpcInvocation(Method method, Object[] arguments, Map<String, String> attachment) {
        this(method.getName(), method.getParameterTypes(), arguments, attachment, null);
    }

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this(methodName, parameterTypes, arguments, null, null);
    }

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments) {
        this(methodName, parameterTypes, arguments, attachments, null);
    }

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments, Invoker<?> invoker) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.arguments = arguments == null ? new Object[0] : arguments;
        this.attachments = attachments == null ? Maps.newHashMap() : attachments;
        this.invoker = invoker;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Map<String, String> getAttachments() {
        return null;
    }

    @Override
    public Invoker<?> getInvoker() {
        return null;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments == null ? new Object[0] : arguments;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments == null ? Maps.newHashMap() : attachments;
    }

    public void setAttachment(String key, String value) {
        if (attachments == null) {
            attachments = Maps.newHashMap();
        }
        attachments.put(key, value);
    }

    public void setAttachmentIfAbsent(String key, String value) {
        if (attachments == null) {
            attachments = Maps.newHashMap();
        }
        attachments.putIfAbsent(key, value);
    }

    public void addAttachments(Map<String, String> attachments) {
        if (attachments == null) {
            return;
        }
        if (this.attachments == null) {
            this.attachments = Maps.newHashMap();
        }
        this.attachments.putAll(attachments);
    }

    public void addAttachmentsIfAbsent(Map<String, String> attachments) {
        if (attachments == null) {
            return;
        }
        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            setAttachmentIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String getAttachment(String key) {
        if (attachments == null) {
            return null;
        }
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        if (attachments == null) {
            return null;
        }
        String value = attachments.get(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }
}
