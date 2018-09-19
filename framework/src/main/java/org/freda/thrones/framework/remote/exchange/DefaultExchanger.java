package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.DefaultExchangeClient;
import org.freda.thrones.framework.remote.client.ExchangeClient;
import org.freda.thrones.framework.remote.handler.DefaultExchangeHandler;
import org.freda.thrones.framework.remote.server.DefaultExchangeServer;
import org.freda.thrones.framework.remote.server.ExchangeServer;
import org.freda.thrones.framework.remote.transport.TransporterKernel;

/**
 * the implement class of exchanger
 */
public class DefaultExchanger implements Exchanger {

    @Override
    public ExchangeServer bind(URL url, ExchangeHandler handler) throws LinkingException {
        return new DefaultExchangeServer(TransporterKernel.bind(url, new DefaultExchangeHandler(handler)));
    }

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) throws LinkingException {
        return new DefaultExchangeClient(TransporterKernel.connect(url, new DefaultExchangeHandler(handler)), true);
    }
}
