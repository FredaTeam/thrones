package org.freda.thrones.framework.manager.invoke;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.exceptions.RpcStatus;
import org.freda.thrones.framework.manager.Invocation;
import org.freda.thrones.framework.manager.Result;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.client.ExchangeClient;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create on 2018/9/4 13:42
 */
@Slf4j
public class ThronesInvoker<T> extends AbstractInvoker<T> {

    private final ExchangeClient[] clients;

    private final String version;

    private final ReentrantLock destroyLock = new ReentrantLock();

    private final Set<Invoker<?>> invokers;


    public ThronesInvoker(Class<T> serviceType, URL url, ExchangeClient[] clients) {
        this(serviceType, url, clients, null);
    }

    public ThronesInvoker(Class<T> serviceType, URL url, ExchangeClient[] clients, Set<Invoker<?>> invokers) {
        super(serviceType, url, new String[]{Constants.PARAMETER.INTERFACE_KEY, Constants.PARAMETER.GROUP_KEY, Constants.PARAMETER.TOKEN_KEY, Constants.PARAMETER.TIMEOUT_KEY});
        this.clients = clients;
        // get version.
        this.version = url.getParam(Constants.PARAMETER.VERSION_KEY, "0.0.0");
        this.invokers = invokers;
    }

    // todo need to support multi clients but now support only one
    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        try {
            ExchangeClient currentClient = clients[0];
            return (Result) currentClient.request(invocation).get();
        } catch (LinkingException e) {
            throw new RpcException(RpcStatus.of(RpcStatus.NETWORK_EXCEPTION, e.getMessage()));
        }
    }

    @Override
    public boolean isAvailable() {
        if (!super.isAvailable()) {
            return false;
        }
        Optional<ExchangeClient> optional = Arrays.stream(clients).filter(ChannelChain::isConnected).findAny();
        return optional.isPresent();
    }

    @Override
    public void destroy() {

        destroyLock.lock();
        try {
            if (super.isDestroyed()) {
                return;
            }
            super.destroy();
            if (invokers != null) {
                invokers.remove(this);
            }
            Arrays.stream(clients).forEach(it -> {
                try {
                    it.close();
                } catch (Throwable e) {
                    log.warn(e.getMessage(), e);
                }
            });
        } finally {
            destroyLock.unlock();
        }
    }
}
