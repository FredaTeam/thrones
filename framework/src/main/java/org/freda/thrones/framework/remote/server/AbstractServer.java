package org.freda.thrones.framework.remote.server;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.AbstractNode;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.ChannelChainHandler;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Create on 2018/9/6 17:14
 */
@Slf4j
public abstract class AbstractServer extends AbstractNode implements Server {

    private InetSocketAddress localAddress;

    public AbstractServer(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, handler);

        localAddress = url.toInetSocketAddress();
        try {
            doOpen();
        } catch (Throwable t) {
            throw new LinkingException(url.toInetSocketAddress(), null, "Failed to bind " + getClass().getSimpleName()
                    + " on " + getLocalAddress() + ", cause: " + t.getMessage(), t);
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public void send(Object message) throws LinkingException {
        send(message, false);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        Collection<ChannelChain> channelChains = getChannelChains();
        for (ChannelChain channelChain : channelChains) {
            if (channelChain.isConnected()) {
                channelChain.send(message, sent);
            }
        }
    }

    // todo need to complete
    @Override
    public void reset(URL url) {
        super.setUrl(getUrl().addParam(url.getParams()));
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            doClose();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void close(int timeout) {
        close();
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;


}
