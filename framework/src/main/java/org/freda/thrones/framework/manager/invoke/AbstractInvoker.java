package org.freda.thrones.framework.manager.invoke;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;
import org.freda.thrones.framework.manager.RpcInvocation;
import org.freda.thrones.framework.manager.RpcResult;
import org.freda.thrones.common.utils.NetUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create on 2018/9/4 10:53
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {

    private final Class<T> type;

    private final URL url;

    private final Map<String, String> attachments;

    private volatile boolean available;

    private AtomicBoolean destroyed = new AtomicBoolean(false);

    public AbstractInvoker(Class<T> type, URL url, Map<String, String> attachments) {
        Preconditions.checkNotNull(type, "type can not be null");
        Preconditions.checkNotNull(url, "url can not be null");

        this.type = type;
        this.url = url;
        this.attachments = attachments == null ? Maps.newHashMap() : attachments;
    }

    public AbstractInvoker(Class<T> type, URL url, String[] keys) {
        this(type, url, convertAttachment(url, keys));
    }

    private static Map<String, String> convertAttachment(URL url, String[] keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        Map<String, String> attachment = Maps.newHashMap();
        for (String key : keys) {
            String value = url.getParam(key);
            if (value != null && value.length() > 0) {
                attachment.put(key, value);
            }
        }
        return attachment;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        if (destroyed.get()) {
            throw new RpcException("Rpc invoker for service " + this + " on consumer " + NetUtils.getLocalHost()
                    + " is destroyed, can not be invoked any more!");
        }
        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        rpcInvocation.setInvoker(this);

        try {
            return doInvoke(rpcInvocation);
        } catch (RpcException rpcException) {
            if (rpcException.isBiz()) {
                return new RpcResult(rpcException);
            }
            throw rpcException;
        } catch (Throwable throwable) {
            return new RpcResult(throwable);
        }
    }

    protected abstract Result doInvoke(Invocation invocation) throws Throwable;

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    protected void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public void destroy() {
        if (!destroyed.compareAndSet(false, true)) {
            return;
        }
        setAvailable(false);
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }
}
