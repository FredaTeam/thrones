package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.AbstractNode;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;

public abstract class AbstractChannelChain extends AbstractNode implements ChannelChain {


    public AbstractChannelChain(URL url, ChannelChainHandler handler) {
        super(url, handler);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        if (closed()) {
            throw new LinkingException(this, "Failed to send message "
                    + (message == null ? "" : message.getClass().getName()) + ":" + message
                    + ", cause: Channel closed. channel: " + getLocalAddress() + " -> " + getRemoteAddress());
        }
    }

    @Override
    public String toString() {
        return getLocalAddress() + " -> " + getRemoteAddress();
    }
}
