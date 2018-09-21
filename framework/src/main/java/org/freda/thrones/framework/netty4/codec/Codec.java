package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import org.freda.thrones.framework.netty4.Netty4ChannelChain;
import org.freda.thrones.framework.remote.ChannelChain;

import java.io.IOException;
import java.util.List;

/**
 * Create on 2018/9/20 11:32
 */
public interface Codec {

    /**
     * encode msg
     * <p>
     * why channelchain?
     * because we can get additional infomation from channelchain
     * but if we use io.netty.channel.channel, we get nothing
     */
    void encode(Netty4ChannelChain channelChain, ByteBuf byteBuf, Object msg) throws IOException;

    /**
     * decode msg
     */
    Object decode(Netty4ChannelChain channelChain, ByteBuf byteBuf) throws IOException;
}
