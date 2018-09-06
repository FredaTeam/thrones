package org.freda.thrones.framework.remote.exchange;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.client.ExchangeClient;
import org.freda.thrones.framework.remote.server.ExchangeServer;

import java.util.Objects;

/**
 * Create on 2018/9/5 11:02
 */
public class ExchangerKernel {

    public static ExchangeServer bind(URL url, ExchangeHandler handler) throws LinkingException {

        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");

        return new DefaultExchanger().bind(url, handler);
    }

    public static ExchangeClient connect(URL url, ExchangeHandler handler) throws LinkingException {
        Preconditions.checkArgument(Objects.nonNull(url), "url can not be null");
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");

        return new DefaultExchanger().connect(url, handler);
    }
}
