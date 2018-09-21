package org.freda.thrones.framework.remote.server;

import org.freda.thrones.framework.remote.exechange.ExechangeChannelChain;
import org.freda.thrones.framework.remote.server.Server;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface ExechangeServer extends Server {

    /**
     * get all exechangeChannelChain
     */
    Collection<ExechangeChannelChain> getExechangeChannels();

    /**
     * get exechangeChannelChain by address
     */
    ExechangeChannelChain getExechangeChannel(InetSocketAddress remoteAddress);

}
