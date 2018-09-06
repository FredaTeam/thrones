package org.freda.thrones.framework.remote.transport;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChainHandler;
import org.freda.thrones.framework.remote.client.Client;
import org.freda.thrones.framework.remote.server.Server;

/**
 * Create on 2018/9/5 14:31
 * transporter layer use by netty
 */
public interface Transporter {

    /**
     * bind to port
     */
    Server bind(URL url, ChannelChainHandler handler) throws LinkingException;

    /**
     * connect to server
     */
    Client connect(URL url, ChannelChainHandler handler) throws LinkingException;
}
