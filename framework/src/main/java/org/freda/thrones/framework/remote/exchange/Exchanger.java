package org.freda.thrones.framework.remote.exchange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;

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
