package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;

import java.util.concurrent.CompletableFuture;

/**
 * Create on 2018/9/5 10:48
 */
public abstract class AbstractExchangeHandler implements ExchangeHandler {

    @Override
    public CompletableFuture<Object> reply(ExchangeChannelChain channel, Object request) throws LinkingException {
        return null;
    }
}
