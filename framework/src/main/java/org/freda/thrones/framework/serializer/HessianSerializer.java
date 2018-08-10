package org.freda.thrones.framework.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer<Object> {
    /**
     * Serialize the given object to binary data.
     *
     * @param o object to serialize
     * @return the equivalent binary data
     */
    @Override
    public byte[] serialize(Object o) throws SerializationException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Hessian2Output ho = new Hessian2Output(byteArrayOutputStream);
        try
        {
            ho.startMessage();
            ho.writeObject(o);
            ho.completeMessage();
            ho.close();
            byteArrayOutputStream.close();
        }
        catch (IOException e)
        {
            throw new SerializationException(e.getMessage(), e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Deserialize an object from the given binary data.
     *
     * @param bytes object binary representation
     * @return the equivalent object instance
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try
        {
            Hessian2Input hi = new Hessian2Input(byteArrayInputStream);
            hi.startMessage();
            Object result = hi.readObject();
            hi.completeMessage();
            hi.close();
            byteArrayInputStream.close();
            return result;
        }
        catch (IOException e)
        {
            throw new SerializationException(e.getMessage(), e);
        }
    }
}
