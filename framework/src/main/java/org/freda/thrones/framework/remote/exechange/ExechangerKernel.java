package org.freda.thrones.framework.remote.exechange;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.ExechangeClient;
import org.freda.thrones.framework.remote.server.ExechangeServer;

import java.util.Objects;

/**
 * Create on 2018/9/5 11:02
 */
public class ExechangerKernel {

    public static ExechangeServer bind(URL url, ExechangeHandler handler) throws LinkingException {

        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");

        return new DefaultExechanger().bind(url, handler);
    }

    public static ExechangeClient connect(URL url, ExechangeHandler handler) throws LinkingException {
        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");

        return new DefaultExechanger().connect(url, handler);
    }
}
