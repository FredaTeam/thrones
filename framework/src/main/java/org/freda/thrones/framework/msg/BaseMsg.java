package org.freda.thrones.framework.msg;

import org.freda.thrones.framework.constants.ThronesTCPConstant;

/**
 * Msg 基类
 */
public abstract class BaseMsg
{
    protected Header header = new Header();

    protected byte[] bodyBytes = new byte[0];

    public BaseMsg()
    {
    }

    /**
     * 消息转化为Bytes
     *
     * @return
     */
    public byte[] toBytes()
    {
        byte[] commandBytes = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN + bodyBytes.length];

        System.arraycopy(header.toBytes(), 0, commandBytes, 0, ThronesTCPConstant.THRONES_MSG_HEADER_LEN);

        System.arraycopy(bodyBytes, 0, commandBytes, ThronesTCPConstant.THRONES_MSG_HEADER_LEN, bodyBytes.length);

        return commandBytes;
    }

    private void read(){

    }

    public void bytesToMsg()
    {
        /**
         * empty method
         */
    }

    public void msgToBytes()
    {
        /**
         * empty method
         */
    }
}
