package org.freda.thrones.framework.netty4;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.server.AbstractServer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class Netty4Server extends AbstractServer {

    public Netty4Server(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, handler);
    }

    @Override
    protected void doOpen() throws Throwable {

    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    public boolean isActve() {
        return false;
    }

    @Override
    public Collection<ChannelChain> getChannelChains() {
        return null;
    }

    @Override
    public ChannelChain getChannelChain(InetSocketAddress address) {
        return null;
    }
}
