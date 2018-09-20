package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import org.freda.thrones.framework.remote.ChannelChain;

/**
 * Create on 2018/9/20 11:38
 */
public class SimpleCodec extends AbstractCodec {


    @Override
    public void encode(ChannelChain channelChain, ByteBuf byteBuf, Object msg) {

    }

    @Override
    public Object decode(ChannelChain channelChain, ByteBuf byteBuf) {
        return null;
    }
}
