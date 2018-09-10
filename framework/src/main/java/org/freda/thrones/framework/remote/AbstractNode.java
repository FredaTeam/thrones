package org.freda.thrones.framework.remote;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;

import java.util.Objects;

/**
 * Create on 2018/9/5 15:24
 */
public abstract class AbstractNode implements NodePoint, ChannelChainHandler {

    private volatile URL url;

    private final ChannelChainHandler handler;

    // being close
    private volatile boolean closing;

    // finish close
    private volatile boolean closed;

    // build connection timeout
    private int timeout;

    // wait async result timeout
    private int connectTimeout;

    public AbstractNode(URL url, ChannelChainHandler handler) {

        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");

        this.url = url;
        this.handler = handler;
        this.timeout = url.getPositiveParam(Constants.PARAMETER.TIMEOUT_KEY, Constants.VALUE.DEFAULT_TIMEOUT);
        this.connectTimeout = url.getPositiveParam(Constants.PARAMETER.CONNECT_TIMEOUT_KEY, Constants.VALUE.DEFAULT_CONNECT_TIMEOUT);
    }

    @Override
    public void onConnected(ChannelChain channelChain) {
        if (closed) {
            return;
        }
        handler.onConnected(channelChain);
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) {
        handler.onDisConnected(channelChain);
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) {
        if (closed) {
            return;
        }
        handler.onReceived(channelChain, message);
    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) {
        if (closed) {
            return;
        }
        handler.onSent(channelChain, message);
    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) {
        handler.onError(channelChain, throwable);
    }

    @Override
    public URL getUrl() {
        return url;
    }

    protected void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public void close(int timeout) {
        close();
    }

    @Override
    public void closing() {
        if (closed) {
            return;
        }
        closing = true;
    }

    @Override
    public boolean closed() {
        return closed;
    }

    public boolean isClosing() {
        return closing && !closed;
    }
}
