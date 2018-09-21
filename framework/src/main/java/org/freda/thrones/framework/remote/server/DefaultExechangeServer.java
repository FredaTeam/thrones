package org.freda.thrones.framework.remote.server;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.exechange.DefaultExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeChannelChain;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create on 2018/9/19 14:35
 */
@Slf4j
public class DefaultExechangeServer implements ExechangeServer {

    private final Server server;

    private AtomicBoolean closed = new AtomicBoolean(false);

    public DefaultExechangeServer(Server server) {
        this.server = server;
    }

    private void startHeartBeatTask() {

    }

    private void stopHeartBeatTask() {

    }

    @Override
    public Collection<ExechangeChannelChain> getExechangeChannels() {
        Collection<ExechangeChannelChain> exechangeChannelChains = Lists.newArrayList();
        Collection<ChannelChain> channelChains = server.getChannelChains();
        if (channelChains != null && !channelChains.isEmpty()) {
            for (ChannelChain channelChain : channelChains) {
                exechangeChannelChains.add(DefaultExechangeChannelChain.getOrAddChannel(channelChain));
            }
        }
        return exechangeChannelChains;
    }

    @Override
    public ExechangeChannelChain getExechangeChannel(InetSocketAddress remoteAddress) {
        return DefaultExechangeChannelChain.getOrAddChannel(server.getChannelChain(remoteAddress));
    }

    @Override
    public boolean isActve() {
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Collection<ChannelChain> getChannelChains() {
        return (Collection) getExechangeChannels();
    }

    @Override
    public ChannelChain getChannelChain(InetSocketAddress address) {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public void send(Object message) throws LinkingException {
        if (closed.get()) {
            throw new LinkingException(this.getLocalAddress(), null, "Failed to send message " + message + ", cause: The server " + getLocalAddress() + " is closed!");
        }
        server.send(message);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        if (closed.get()) {
            throw new LinkingException(this.getLocalAddress(), null, "Failed to send message " + message + ", cause: The server " + getLocalAddress() + " is closed!");
        }
        server.send(message, sent);
    }

    @Override
    public void reset(URL url) {

    }

    @Override
    public void close() {
        doClose();
        server.close();
    }

    @Override
    public void close(int timeout) {
        closing();
        if (timeout > 0) {
            final long max = (long) timeout;
            final long start = System.currentTimeMillis();
            while (DefaultExechangeServer.this.isRunning() && System.currentTimeMillis() - start < max) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        doClose();
        server.close(timeout);
    }

    private boolean isRunning() {
        Collection<ChannelChain> channels = getChannelChains();
        return channels.stream().anyMatch(ChannelChain::isConnected);
    }

    @Override
    public void closing() {
        server.closing();
    }

    @Override
    public boolean closed() {
        return server.closed();
    }

    private void doClose() {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        stopHeartBeatTask();
    }
}
