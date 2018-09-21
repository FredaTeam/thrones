package org.freda.thrones.framework.remote.exechange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.Closable;
import org.freda.thrones.framework.remote.future.CommonFuture;

/**
 * actions define of exechange
 */
public interface ExechangeChannelChain extends ChannelChain, Closable {

    /**
     * send request
     */
    CommonFuture request(Object request) throws LinkingException;

    /**
     * send request in timeout
     */
    CommonFuture request(Object request, int timeout) throws LinkingException;

    /**
     * get message handler
     */
    ExechangeHandler getExechangeHandler();

    /**
     * close channel chain
     */
    @Override
    void close();

    /**
     * close channel chain
     */
    @Override
    void close(int timeout);
}
