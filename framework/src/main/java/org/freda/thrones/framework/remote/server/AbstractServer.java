package org.freda.thrones.framework.remote.server;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.remote.AbstractNode;
import org.freda.thrones.framework.remote.ChannelChainHandler;

/**
 * Create on 2018/9/6 17:14
 */
public abstract class AbstractServer extends AbstractNode {


    public AbstractServer(URL url, ChannelChainHandler handler) {
        super(url, handler);
    }
}
