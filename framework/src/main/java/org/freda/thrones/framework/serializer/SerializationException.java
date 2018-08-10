package org.freda.thrones.framework.serializer;

/**
 * 序列化异常
 */
public class SerializationException extends RuntimeException
{
    /**
     * Constructs a new <code>SerializationException</code> instance.
     *
     * @param msg
     * @param cause
     */
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new <code>SerializationException</code> instance.
     *
     * @param msg
     */
    public SerializationException(String msg) {
        super(msg);
    }
}
