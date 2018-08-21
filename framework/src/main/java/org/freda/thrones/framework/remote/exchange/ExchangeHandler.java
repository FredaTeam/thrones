package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChainHandler;

import java.util.concurrent.CompletableFuture;

public interface ExchangeHandler extends ChannelChainHandler {

    /**
     * ref: http://www.wowfree.cn/2018/04/08/CompletableFuture01/
     */
    CompletableFuture<Object> reply(ExchangeChannelChain channel, Object request) throws LinkingException;

}
