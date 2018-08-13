package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.Closable;
import org.freda.thrones.framework.remote.future.CommonFuture;

public interface ExchangeChannelChain extends ChannelChain, Closable {

    CommonFuture request(Object request) throws LinkingException;

    CommonFuture request(Object request, int timeout) throws LinkingException;

    @Override
    void close();

    @Override
    void close(int timeout);
}
