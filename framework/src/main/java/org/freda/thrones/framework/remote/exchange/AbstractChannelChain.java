package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.ChannelChainHandler;

public abstract class AbstractChannelChain implements ChannelChain {

    private final ChannelChainHandler handler;

    private volatile URL url;

    // closing closed means the process is being closed and close is finished
    private volatile boolean closing;

    private volatile boolean closed;


    public AbstractChannelChain(URL url, ChannelChainHandler handler) {

        if (url == null || handler == null) {
            throw new IllegalArgumentException("url or channelChainHandler can not be null.");
        }
        this.url = url;
        this.handler = handler;
    }

    @Override
    public boolean closed() {
        return closed;
    }

    @Override
    public void close() {

        closing();

        this.closed = true;
    }

    @Override
    public void close(int timeout) {

        close();
    }

    @Override
    public void closing() {
        if (!this.closed) {
            this.closing = true;
        }
    }

    public boolean isClosing() {
        return closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public void send(Object message) throws LinkingException {
        //TODO what is sent ?
        send(message, false);
    }

    @Override
    public void reset(URL url) {

    }
}
