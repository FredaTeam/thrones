package org.freda.thrones.framework.remote.exechange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;

import java.util.concurrent.CompletableFuture;

public interface ExechangeHandler extends ChannelChainHandler {

    /**
     * ref: http://www.wowfree.cn/2018/04/08/CompletableFuture01/
     */
    CompletableFuture<Object> reply(ExechangeChannelChain channel, Object request) throws LinkingException;

}
