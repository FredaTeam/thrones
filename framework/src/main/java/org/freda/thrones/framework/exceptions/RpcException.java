package org.freda.thrones.framework.exceptions;

public class RpcException extends RuntimeException {

    private RpcStatus rpcStatus;

    public RpcException(RpcStatus rpcStatus) {
        super();
        this.rpcStatus = rpcStatus;
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public boolean isBiz() {
        return rpcStatus.getCode() == RpcStatus.BIZ_EXCEPTION;
    }

    public boolean isForbidded() {
        return rpcStatus.getCode() == RpcStatus.FORBIDDEN_EXCEPTION;
    }

    public boolean isTimeout() {
        return rpcStatus.getCode() == RpcStatus.TIMEOUT_EXCEPTION;
    }

    public boolean isNetwork() {
        return rpcStatus.getCode() == RpcStatus.NETWORK_EXCEPTION;
    }

    public boolean isSerialization() {
        return rpcStatus.getCode() == RpcStatus.SERIALIZATION_EXCEPTION;
    }

}
