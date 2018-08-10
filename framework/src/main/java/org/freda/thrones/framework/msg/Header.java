package org.freda.thrones.framework.msg;

import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.enums.MsgCommandEnum;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.utils.NumberBytesConvertUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息头
 */
public class Header
{

    /**
     * 协议标识
     * @see org.freda.thrones.framework.constants.ThronesTCPConstant
     */
    private String magic;
    /**
     * 协议命令
     */
    private MsgCommandEnum command;
    /**
     * 调用状态
     */
    private MsgStatusEnum status;
    /**
     * 调用序列号
     */
    private Long sequence;
    /**
     * 消息长度 头 + 体
     */
    private int totalLen;


    public Header()
    {
        this.magic = ThronesTCPConstant.THRONES_TCP_MAGIC;
    }

    public Header(MsgCommandEnum command, MsgStatusEnum status)
    {
        this.magic = ThronesTCPConstant.THRONES_TCP_MAGIC;
        this.command = command;
        this.status = status;
        this.sequence = getSeq();
    }

    public Header(MsgCommandEnum command, MsgStatusEnum status, Long sequence) {
        this.command = command;
        this.status = status;
        this.sequence = sequence;
    }

    public Header(String magic, MsgCommandEnum command, MsgStatusEnum status, Long sequence, int totalLen)
    {
        this.magic = magic;
        this.command = command;
        this.status = status;
        this.sequence = sequence;
        this.totalLen = totalLen;
    }



    protected byte[] toBytes()
    {
        byte[] headerBytes = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN];

        System.arraycopy(magic.getBytes(), 0, headerBytes, 0, 7);

        headerBytes[7] = command.getCommand();

        headerBytes[8] = status.getStatus();

        System.arraycopy(NumberBytesConvertUtils.longToBytes8(sequence), 0, headerBytes, 9, 8);

        System.arraycopy(NumberBytesConvertUtils.intToBytes4(totalLen), 0, headerBytes, 17, 4);

        return headerBytes;
    }

    /**
     * bytes[21] to Header
     *
     * @param bytes stream
     * @return Header
     */
    public static Header read(byte[] bytes)
    {
        byte[] temp = new byte[7];
        System.arraycopy(bytes, 0, temp, 0, 7);
        String magic = new String(temp);

        MsgCommandEnum command = MsgCommandEnum.commandOf(bytes[7]);
        MsgStatusEnum status = MsgStatusEnum.statusOf(bytes[8]);

        temp = new byte[8];
        System.arraycopy(bytes, 9, temp, 0, 8);
        long sequence = NumberBytesConvertUtils.bytes8ToLong(temp);

        temp = new byte[4];
        System.arraycopy(bytes, 17, temp, 0, 4);
        int totalLen = NumberBytesConvertUtils.bytes4ToInt(temp);

        return new Header(magic, command, status, sequence, totalLen);
    }

    public MsgCommandEnum getCommand() {
        return command;
    }

    public void setCommand(MsgCommandEnum command) {
        this.command = command;
    }

    public MsgStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MsgStatusEnum status) {
        this.status = status;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    private static volatile AtomicLong seq = new AtomicLong(0);

    private long getSeq()
    {
        seq.compareAndSet(Long.MAX_VALUE - 1L, 0L);

        return seq.incrementAndGet();
    }
}
