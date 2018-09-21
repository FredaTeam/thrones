package org.freda.thrones.framework.remote.exechange;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.DefaultExechangeClient;
import org.freda.thrones.framework.remote.client.ExechangeClient;
import org.freda.thrones.framework.remote.handler.DefaultExechangeHandler;
import org.freda.thrones.framework.remote.server.DefaultExechangeServer;
import org.freda.thrones.framework.remote.server.ExechangeServer;
import org.freda.thrones.framework.remote.transport.TransporterKernel;

/**
 * the implement class of exechanger
 */
public class DefaultExechanger implements Exechanger {

    @Override
    public ExechangeServer bind(URL url, ExechangeHandler handler) throws LinkingException {
        return new DefaultExechangeServer(TransporterKernel.bind(url, new DefaultExechangeHandler(handler)));
    }

    @Override
    public ExechangeClient connect(URL url, ExechangeHandler handler) throws LinkingException {
        return new DefaultExechangeClient(TransporterKernel.connect(url, new DefaultExechangeHandler(handler)), true);
    }
}
