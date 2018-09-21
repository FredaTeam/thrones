package org.freda.thrones.framework.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.exechange.AbstractClient;
import org.freda.thrones.framework.remote.handler.HandlerKernel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Client for Netty4.
 */
@Slf4j
public class Netty4Client extends AbstractClient {

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.VALUE.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", true));

    private Bootstrap bootstrap;

    private volatile Channel channel;

    public Netty4Client(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, HandlerKernel.wrap(handler, url));
    }

    @Override
    protected void doOen() throws Throwable {

        final Netty4ClientHandler clientHandler = new Netty4ClientHandler(getUrl(), this);

        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer() {
            Netty4CodecHandler codecHandler = new Netty4CodecHandler(getUrl(), Netty4Client.this);

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast("decoder",codecHandler.getDecoder())
                        .addLast("encoder",codecHandler.getEncoder())
                        .addLast("", clientHandler);
            }
        });
    }

    @Override
    protected void doClose() throws Throwable {

        //empty
    }

    @Override
    protected void doConnect() throws Throwable {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(getUrl().getHost(), getUrl().getPort()));

        boolean ret = future.awaitUninterruptibly(Constants.DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        if (ret && future.isSuccess()) {
            Channel newChannel = future.channel();
            try {
                Channel oldChannel = Netty4Client.this.channel;
                try {
                    if (oldChannel != null) {
                        oldChannel.close();
                    }
                } finally {
                    Netty4ChannelChain.removeChannelIfDisconnected(oldChannel);
                }

            } finally {
                if (Netty4Client.this.closed()) {
                    try {
                        newChannel.close();
                    } finally {
                        Netty4Client.this.channel = null;
                        Netty4ChannelChain.removeChannelIfDisconnected(newChannel);
                    }
                } else {
                    Netty4Client.this.channel = newChannel;
                }
            }
        } else {
            throw new LinkingException(this, "can not connect to server. url : " + getUrl(), future.cause());
        }


    }

    @Override
    protected void doDisConnect() throws Throwable {

        Netty4ChannelChain.removeChannelIfDisconnected(channel);
    }

    @Override
    protected ChannelChain getChannelChain() {
        Channel c = channel;
        if (c == null || !c.isActive()) {
            return null;
        }
        return Netty4ChannelChain.getOrAddChannel(c, getUrl(), this);
    }
}
