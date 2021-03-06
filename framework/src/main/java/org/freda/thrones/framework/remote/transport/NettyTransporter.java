package org.freda.thrones.framework.remote.transport;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.netty4.Netty4Client;
import org.freda.thrones.framework.netty4.Netty4Server;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.client.Client;
import org.freda.thrones.framework.remote.server.Server;

/**
 * Create on 2018/9/5 14:37
 */
public class NettyTransporter implements Transporter {

    @Override
    public Server bind(URL url, ChannelChainHandler handler) throws LinkingException {
        return new Netty4Server(url, handler);
    }

    @Override
    public Client connect(URL url, ChannelChainHandler handler) throws LinkingException {
        return new Netty4Client(url, handler);
    }
}
