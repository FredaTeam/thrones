package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.Closable;
import org.freda.thrones.framework.remote.future.Future;

public interface ExchangeChannelChain extends ChannelChain, Closable {

    Future request(Object request) throws LinkingException;

    Future request(Object request, int timeout) throws LinkingException;

    @Override
    void close();

    @Override
    void close(int timeout);
}
