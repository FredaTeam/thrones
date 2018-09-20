package org.freda.thrones.framework.remote.server;

import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.NodePoint;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface Server extends NodePoint {

    /**
     * alive or not
     */
    boolean isActve();

    /**
     * get all channels
     */
    Collection<ChannelChain> getChannelChains();

    /**
     * get channelChain by ip:port
     */
    ChannelChain getChannelChain(InetSocketAddress address);
}
