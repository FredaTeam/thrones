package org.freda.thrones.framework.remote.exechange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.ExechangeClient;
import org.freda.thrones.framework.remote.server.ExechangeServer;

public interface Exechanger {

    /**
     * bind port for server
     */
    ExechangeServer bind(URL url, ExechangeHandler handler) throws LinkingException;

    /**
     * connect server for client
     */
    ExechangeClient connect(URL url, ExechangeHandler handler) throws LinkingException;
}
