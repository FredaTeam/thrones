package org.freda.thrones.framework.remote.handler;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.exchange.DefaultExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeHandler;

import java.util.Objects;

/**
 * Create on 2018/9/8 09:43
 */
@Slf4j
public class DefaultExchangeHandler implements ChannelChainHandlerDelegate {

    private final ExchangeHandler handler;

    public DefaultExchangeHandler(ExchangeHandler handler) {
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");
        this.handler = handler;
    }

    @Override
    public void onConnected(ChannelChain channelChain) throws LinkingException {
        ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onConnected(exchangeChannelChain);
        } finally {
            DefaultExchangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) throws LinkingException {
        ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onDisConnected(exchangeChannelChain);
        } finally {
            DefaultExchangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {

    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) throws LinkingException {

    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) throws LinkingException {

    }

    @Override
    public ChannelChainHandler getHandler() {
        if (handler instanceof ChannelChainHandlerDelegate) {
            return ((ChannelChainHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }
}
