package org.freda.thrones.framework.serializer;

import java.io.*;

public class DefaultJDKSerializer implements Serializer<Object> {



    /**
     * Serialize the given object to binary data.
     *
     * @param o object to serialize
     * @return the equivalent binary data
     */
    @Override
    public byte[] serialize(Object o) throws SerializationException {

        if (!(o instanceof Serializable))
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
                    "but received an object of type [" + o.getClass().getName() + "]");
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);

        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();

            return byteStream.toByteArray();
        }
        catch (IOException e)
        {
            throw new SerializationException(e.getMessage(), e);
        }
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
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

        ObjectInputStream objectInputStream = null;

        try
        {
            objectInputStream = new ObjectInputStream(byteStream);

            return objectInputStream.readObject();
        }
        catch (Exception e)
        {
            throw new SerializationException(e.getMessage(), e);
        }
        finally
        {
            try
            {
                objectInputStream.close();
            }
            catch (IOException e)
            {
                throw new SerializationException(e.getMessage(), e);
            }
        }
    }
}
