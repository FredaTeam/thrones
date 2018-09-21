package org.freda.thrones.framework.netty4;

import com.google.common.collect.Sets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.handler.HandlerKernel;
import org.freda.thrones.framework.remote.server.AbstractServer;
import org.freda.thrones.framework.utils.NetUtils;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class Netty4Server extends AbstractServer {

    private static final String THREAD_NAME = "ThronesServerHandler";

    private ServerBootstrap serverBootstrap;

    private Map<String, ChannelChain> channelChains;

    private Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public Netty4Server(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, HandlerKernel.wrap(handler, setThreadName(url, THREAD_NAME)));
    }

    private static URL setThreadName(URL url, String defaultName) {
        String name = url.getParam(Constants.PARAMETER.THREAD_NAME_KEY, defaultName);
        name = name + "-" + url.getAddress();
        url = url.addParam(Constants.PARAMETER.THREAD_NAME_KEY, name);
        return url;
    }

    @Override
    protected void doOpen() throws Throwable {
        serverBootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(Constants.VALUE.DEFAULT_IO_THREADS,
                new DefaultThreadFactory("NettyServerWorker", true));

        final Netty4ServerHandler netty4ServerHandler = new Netty4ServerHandler(getUrl(), this);
        channelChains = netty4ServerHandler.getChannelChains();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        Netty4CodecHandler codecHandler = new Netty4CodecHandler(getUrl(), Netty4Server.this);
                        ch.pipeline()
                                .addLast("decoder", codecHandler.getDecoder())
                                .addLast("encoder", codecHandler.getEncoder())
                                .addLast("handler", netty4ServerHandler);
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(getUrl().getHost(), getUrl().getPort()));
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            Collection<ChannelChain> activeChannels = getChannelChains();
            if (activeChannels != null && activeChannels.size() > 0) {
                for (ChannelChain channelChain : activeChannels) {
                    try {
                        channelChain.close();
                    } catch (Throwable e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            if (serverBootstrap != null) {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            if (channelChains != null) {
                channelChains.clear();
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public boolean isActve() {
        return channel.isActive();
    }

    @Override
    public Collection<ChannelChain> getChannelChains() {
        Collection<ChannelChain> activeChannelChains = Sets.newHashSet();
        for (ChannelChain channelChain : channelChains.values()) {
            if (channelChain.isConnected()) {
                activeChannelChains.add(channelChain);
            } else {
                channelChains.remove(NetUtils.toAddressString(channelChain.getRemoteAddress()));
            }
        }
        return activeChannelChains;
    }

    @Override
    public ChannelChain getChannelChain(InetSocketAddress address) {
        return channelChains.get(NetUtils.toAddressString(address));

    }
}
