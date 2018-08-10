package org.freda.thrones.framework.enums;

import org.freda.thrones.framework.serializer.DefaultJDKSerializer;
import org.freda.thrones.framework.serializer.HessianSerializer;
import org.freda.thrones.framework.serializer.Serializer;

import java.util.Arrays;

public enum MsgSerializerEnum
{

    HESSIAN("hessian", HessianSerializer.class),

    JDK("jdk", DefaultJDKSerializer.class);

    MsgSerializerEnum(String name, Class clazz)
    {
        this.name = name;

        this.clazz = clazz;
    }

    private String name;

    private Class<Serializer> clazz;

    public String getName() {
        return name;
    }

    public Class<Serializer> getClazz() {
        return clazz;
    }


    public static MsgSerializerEnum nameOf(String name)
    {
        return Arrays.stream(MsgSerializerEnum.values())
                .filter(t -> t.getName().equals(name))
                .findFirst().get();
    }
}
