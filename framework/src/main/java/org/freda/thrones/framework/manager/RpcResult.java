package org.freda.thrones.framework.manager;

/**
 * Create on 2018/9/4 13:23
 */
public class RpcResult extends AbstractResult {

    public RpcResult(Object result) {
        this.result = result;
    }

    public RpcResult(Throwable throwable) {
        this.exception = throwable;
    }

    @Override
    public Object getValue() {
        return result;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }

    @Override
    public Object recreate() throws Throwable {
        if (hasException()) {
            throw exception;
        }
        return result;
    }
}
