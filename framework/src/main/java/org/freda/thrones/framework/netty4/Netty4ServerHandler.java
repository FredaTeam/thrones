package org.freda.thrones.framework.netty4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.common.utils.NetUtils;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;

/**
 * Create on 2018/9/19 15:59
 */
@ChannelHandler.Sharable
public class Netty4ServerHandler extends ChannelDuplexHandler {

    private final Map<String, ChannelChain> channelChains = Maps.newConcurrentMap();

    private final URL url;

    private final ChannelChainHandler handler;

    public Netty4ServerHandler(URL url, ChannelChainHandler handler) throws LinkingException {
        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "channelChainHandler can not be null");

        this.url = url;
        this.handler = handler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Netty4ChannelChain netty4ChannelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(), url, handler);

        try {
            handler.onReceived(netty4ChannelChain, msg);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        Netty4ChannelChain netty4ChannelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.onSent(netty4ChannelChain, msg);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Netty4ChannelChain netty4ChannelChain = Netty4ChannelChain.getOrAddChannel(channel, url, handler);
        try {
            if (Objects.nonNull(netty4ChannelChain)) {
                channelChains.put(NetUtils.toAddressString((InetSocketAddress) channel.remoteAddress()), netty4ChannelChain);
            }
            handler.onConnected(netty4ChannelChain);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(channel);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Netty4ChannelChain netty4ChannelChain = Netty4ChannelChain.getOrAddChannel(channel, url, handler);
        try {
            channelChains.remove(NetUtils.toAddressString((InetSocketAddress) channel.remoteAddress()));
            handler.onDisConnected(netty4ChannelChain);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(channel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        Netty4ChannelChain netty4ChannelChain = Netty4ChannelChain.getOrAddChannel(channel, url, handler);
        try {
            handler.onError(netty4ChannelChain, cause);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(channel);
        }
    }

    public Map<String, ChannelChain> getChannelChains() {
        return channelChains;
    }
}
