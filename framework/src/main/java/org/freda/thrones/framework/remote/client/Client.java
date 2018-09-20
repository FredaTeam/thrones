package org.freda.thrones.framework.remote.client;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.NodePoint;

public interface Client extends ChannelChain, NodePoint {

    /**
     * reconnect from client
     */
    void reconnect() throws LinkingException;
}
