package org.freda.thrones.framework.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.freda.thrones.framework.msg.BaseMsg;

import java.util.List;

public class MsgCoder extends ByteToMessageCodec<BaseMsg>
{
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

    }
}
