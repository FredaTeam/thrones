package org.freda.thrones.framework.serializer;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.enums.MsgSerializerEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory
 */
@Slf4j
public class SerializerFactory
{
    /**
     * Serializers Constant
     */
    private static final Map<Class, Serializer> serializers = new ConcurrentHashMap<>();

    /**
     * Auto put the instance to constant.
     */
    static
    {
        for (MsgSerializerEnum msgSerializerEnum : MsgSerializerEnum.values())
        {
            try
            {
                serializers.put(msgSerializerEnum.getClazz(), msgSerializerEnum.getClazz().newInstance());
            }
            catch (Exception e)
            {
                 log.error("init serializers error", e);
            }

        }
    }

    /**
     * get instance.
     *
     * @param serializerEnum constant key.
     * @return serializer instance.
     */
    public static Serializer getSerializer(MsgSerializerEnum serializerEnum)
    {
        return serializers.get(serializerEnum.getClazz());
    }
}
