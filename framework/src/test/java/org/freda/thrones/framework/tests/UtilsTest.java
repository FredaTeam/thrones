package org.freda.thrones.framework.tests;

import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.enums.MsgSerializerEnum;
import org.freda.thrones.framework.serializer.HessianSerializer;
import org.freda.thrones.framework.serializer.SerializerFactory;
import org.junit.Test;



public class UtilsTest {

    @Test
    public void timeTest()
    {
        System.out.println(((MsgSerializerEnum)new HessianSerializer().deserialize(new HessianSerializer().serialize(MsgSerializerEnum.HESSIAN))).getName());

    }
}
