package org.freda.thrones.framework.remote;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Create on 2018/9/5 14:45
 */
@Slf4j
public class ChannelChainHandlerDispatcher implements ChannelChainHandler {

    private final Collection<ChannelChainHandler> channelHandlers = new CopyOnWriteArraySet<ChannelChainHandler>();

    public ChannelChainHandlerDispatcher() {
    }

    public ChannelChainHandlerDispatcher(ChannelChainHandler... handlers) {
        this(handlers == null ? null : Arrays.asList(handlers));
    }

    public ChannelChainHandlerDispatcher(Collection<ChannelChainHandler> handlers) {
        if (handlers != null && !handlers.isEmpty()) {
            this.channelHandlers.addAll(handlers);
        }
    }

    public Collection<ChannelChainHandler> getChannelHandlers() {
        return channelHandlers;
    }

    @Override
    public void onConnected(ChannelChain channelChain) {
        channelHandlers.forEach(it -> {
            try {
                it.onConnected(channelChain);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) {
        channelHandlers.forEach(it -> {
            try {
                it.onDisConnected(channelChain);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) {
        channelHandlers.forEach(it -> {
            try {
                it.onReceived(channelChain, message);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) {
        channelHandlers.forEach(it -> {
            try {
                it.onSent(channelChain, message);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) {
        channelHandlers.forEach(it -> {
            try {
                it.onError(channelChain, throwable);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        });
    }
}
