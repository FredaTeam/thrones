package org.freda.thrones.framework.remote.server;

import org.freda.thrones.framework.remote.exchange.ExchangeChannelChain;
import org.freda.thrones.framework.remote.server.Server;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface ExchangeServer extends Server {

    /**
     * get all exchangeChannelChain
     */
    Collection<ExchangeChannelChain> getExchangeChannels();

    /**
     * get exchangeChannelChain by address
     */
    ExchangeChannelChain getExchangeChannel(InetSocketAddress remoteAddress);

}
