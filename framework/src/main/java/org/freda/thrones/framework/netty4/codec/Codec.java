package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import org.freda.thrones.framework.remote.ChannelChain;

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
    void encode(ChannelChain channelChain, ByteBuf byteBuf, Object msg);

    /**
     * decode msg
     */
    Object decode(ChannelChain channelChain, ByteBuf byteBuf);
}
