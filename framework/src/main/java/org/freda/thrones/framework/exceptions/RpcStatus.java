package org.freda.thrones.framework.exceptions;

/**
 * Create on 2018/9/4 11:51
 */
public class RpcStatus {

    private int code;

    private String message;

    public static RpcStatus of(int code) {
        return new RpcStatus(code);
    }

    public static RpcStatus of(int code, String message) {
        return new RpcStatus(code, message);
    }

    public RpcStatus(int code) {
        this(code, null);
    }

    public RpcStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final int UNKNOWN_EXCEPTION = 0;
    public static final int NETWORK_EXCEPTION = 1;
    public static final int TIMEOUT_EXCEPTION = 2;
    public static final int BIZ_EXCEPTION = 3;
    public static final int FORBIDDEN_EXCEPTION = 4;
    public static final int SERIALIZATION_EXCEPTION = 5;
}
