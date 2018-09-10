package org.freda.thrones.framework.manager;

import org.freda.thrones.framework.manager.invoke.Invoker;

import java.util.Map;

/**
 * Create on 2018/9/3 15:42
 */
public interface Invocation {


    /**
     * get method name.
     */
    String getMethodName();

    /**
     * get parameter types.
     */
    Class<?>[] getParameterTypes();

    /**
     * get arguments.
     */
    Object[] getArguments();

    /**
     * get attachments.
     */
    Map<String, String> getAttachments();

    /**
     * get attachment by key.
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     */
    String getAttachment(String key, String defaultValue);

    /**
     * get the invoker in current context.
     */
    Invoker<?> getInvoker();
}
