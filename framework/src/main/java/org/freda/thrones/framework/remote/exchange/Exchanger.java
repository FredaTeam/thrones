package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.ExchangeClient;
import org.freda.thrones.framework.remote.server.ExchangeServer;

public interface Exchanger {

    /**
     * bind port for server
     */
    ExchangeServer bind(URL url, ExchangeHandler handler) throws LinkingException;

    /**
     * connect server for client
     */
    ExchangeClient connect(URL url, ExchangeHandler handler) throws LinkingException;
}
