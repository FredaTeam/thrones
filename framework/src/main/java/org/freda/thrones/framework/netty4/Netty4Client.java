package org.freda.thrones.framework.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.exchange.AbstractClient;

/**
 * Client for Netty4.
 */
@Slf4j
public class Netty4Client extends AbstractClient {

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.VALUE.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", true));

    private Bootstrap bootstrap;

    private volatile Channel channel;

    public Netty4Client(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, handler);
    }

    @Override
    protected void doOen() throws Throwable {

    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {

    }

    @Override
    protected void doDisConnect() throws Throwable {

    }

    @Override
    protected ChannelChain getChannelChain() {
        return null;
    }
}
