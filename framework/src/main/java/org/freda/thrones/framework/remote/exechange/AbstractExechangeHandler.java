package org.freda.thrones.framework.remote.exechange;

import org.freda.thrones.framework.exceptions.LinkingException;

import java.util.concurrent.CompletableFuture;

/**
 * Create on 2018/9/5 10:48
 */
public abstract class AbstractExechangeHandler implements ExechangeHandler {

    @Override
    public CompletableFuture<Object> reply(ExechangeChannelChain channel, Object request) throws LinkingException {
        return null;
    }
}
