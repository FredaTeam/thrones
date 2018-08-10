package org.freda.thrones.framework.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.msg.BaseMsg;
import org.freda.thrones.framework.msg.Header;

import java.util.List;

public class MsgCoder extends ByteToMessageCodec<BaseMsg>
{


    /**
     *
     */
    public MsgCoder() {
    }

    /**
     * @param ctx
     * @param msg
     * @param out
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, BaseMsg msg, ByteBuf out) throws Exception
    {
        out.writeBytes(msg.toBytes());

        ctx.flush();
    }

    /**
     * @param ctx
     * @param in
     * @param out
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (in.readableBytes() < ThronesTCPConstant.THRONES_MSG_HEADER_LEN)
        {
            return;
        }
        in.markReaderIndex();
        Header header = Header.read(in.readBytes(ThronesTCPConstant.THRONES_MSG_HEADER_LEN).array());

        if (in.readableBytes() < header.getTotalLen() - ThronesTCPConstant.THRONES_MSG_HEADER_LEN)
        {
            in.resetReaderIndex();
            return;
        }
        out.add(header.getCommand().msgInstanceOf(header, in.readBytes(header.getTotalLen() - ThronesTCPConstant.THRONES_MSG_HEADER_LEN).array()));
    }
}
