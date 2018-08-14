package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChainlHandler;

import java.util.concurrent.CompletableFuture;

public interface ExchangeHandler extends ChannelChainlHandler {

    /**
     * ref: http://www.wowfree.cn/2018/04/08/CompletableFuture01/
     */
    CompletableFuture<Object> reply(ExchangeChannelChain channel, Object request) throws LinkingException;

}
