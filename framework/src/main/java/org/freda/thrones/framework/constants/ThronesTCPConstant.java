package org.freda.thrones.framework.constants;

import org.freda.thrones.framework.enums.MsgSerializerEnum;

public class ThronesTCPConstant {

    /**
     * msg magic
     */
    public static final String THRONES_TCP_MAGIC = "thrones";
    /**
     * Header len
     */
    public static final int THRONES_MSG_HEADER_LEN = 22;
    /**
     * default serializer.
     */
    public static final MsgSerializerEnum DEFAULT_SERIALIZER = MsgSerializerEnum.HESSIAN;
}
