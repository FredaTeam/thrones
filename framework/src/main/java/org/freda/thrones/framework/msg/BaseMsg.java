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

    public BaseMsg(Header header, byte[] bodyBytes)
    {
        this.header = header;

        this.bodyBytes = bodyBytes;

        this.bytesToMsg();
    }



    /**
     * 消息转化为Bytes
     *
     * @return
     */
    public byte[] toBytes()
    {
        msgToBytes();

        byte[] commandBytes = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN + bodyBytes.length];

        System.arraycopy(header.toBytes(), 0, commandBytes, 0, ThronesTCPConstant.THRONES_MSG_HEADER_LEN);

        System.arraycopy(bodyBytes, 0, commandBytes, ThronesTCPConstant.THRONES_MSG_HEADER_LEN, bodyBytes.length);

        return commandBytes;
    }

    /**
     * read bytes to msg.
     */
    protected void bytesToMsg()
    {
        /**
         * empty method
         */
    }

    /**
     * bean to bodyBytes
     */
    protected void msgToBytes()
    {
        /**
         * empty method
         */
    }

    public Header getHeader() {
        return header;
    }
}
