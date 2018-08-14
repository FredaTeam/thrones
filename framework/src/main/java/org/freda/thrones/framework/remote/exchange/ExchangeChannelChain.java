package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.Closable;
import org.freda.thrones.framework.remote.future.CommonFuture;

/**
 * actions define of exchange
 */
public interface ExchangeChannelChain extends ChannelChain, Closable {

    /**
     * send request
     */
    CommonFuture request(Object request) throws LinkingException;

    /**
     * send request in timeout
     */
    CommonFuture request(Object request, int timeout) throws LinkingException;

    @Override
    void close();

    @Override
    void close(int timeout);
}
