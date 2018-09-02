package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.ExchangeClient;
import org.freda.thrones.framework.remote.server.ExchangeServer;

/**
 * the implement class of exchanger
 */
public class DefaultExchanger implements Exchanger {
    @Override
    public ExchangeServer bind(URL url, ExchangeHandler handler) throws LinkingException {
        return null;
    }

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) throws LinkingException {
        return null;
    }
}
