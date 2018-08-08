package org.freda.thrones.framework.constants.enums;

import java.util.Arrays;

public enum MsgStatusEnum
{
    SUCCESS((byte) 1, "success"),

    ERROR((byte) 2, "error"),

    TIMEOUT((byte) 3, "time out");


    private byte status;

    private String text;

    MsgStatusEnum(byte status, String text)
    {
        this.status = status;

        this.text = text;
    }

    public static MsgStatusEnum statusOf(byte status)
    {
        return Arrays.stream(MsgStatusEnum.values())
                .filter(t -> t.status == status)
                .findFirst().get();
    }

    public byte getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
