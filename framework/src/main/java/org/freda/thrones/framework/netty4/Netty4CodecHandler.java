package org.freda.thrones.framework.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.netty4.codec.Codec;
import org.freda.thrones.framework.netty4.codec.ThroneCodec;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;

import java.io.IOException;
import java.util.List;

/**
 * Create on 2018/9/21 10:23
 */
public class Netty4CodecHandler {

    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    private final URL url;

    private final ChannelChainHandler handler;

    private final Codec codec = new ThroneCodec();

    public Netty4CodecHandler(URL url, ChannelChainHandler handler) {
        this.url = url;
        this.handler = handler;
    }


    private class InternalEncoder extends MessageToByteEncoder {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Channel ch = ctx.channel();
            Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ch, url, handler);
            try {
                codec.encode(channelChain, out, msg);
            } finally {
                Netty4ChannelChain.removeChannelIfDisconnected(ch);
            }
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            Channel ch = ctx.channel();
            Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ch, url, handler);
            try {
                Object msg = codec.decode(channelChain, in);
                out.add(msg);
            } catch (IOException e) {
                throw e;
            } finally {
                Netty4ChannelChain.removeChannelIfDisconnected(ch);
            }
        }
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }
}
