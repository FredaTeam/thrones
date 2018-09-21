package org.freda.thrones.framework.remote.exechange;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.AbstractNode;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.ThronesThreadFactory;
import org.freda.thrones.framework.remote.client.Client;
import org.freda.thrones.framework.utils.NetUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create on 2018/8/18 14:23
 */
@Slf4j
public abstract class AbstractClient extends AbstractNode implements Client {


    private final Lock CONNECT_LOCK = new ReentrantLock();

    private final AtomicInteger RECONNECT_COUNT = new AtomicInteger(0);

    private static final ScheduledThreadPoolExecutor reconnectExecutorService = new ScheduledThreadPoolExecutor(2, new ThronesThreadFactory("ClientReconnectTimer", true));

    private volatile ScheduledFuture<?> reconnectExecutorFuture = null;

    public AbstractClient(URL url, ChannelChainHandler handler) throws LinkingException {
        super(url, handler);

        try {
            doOen();
        } catch (Throwable throwable) {
            close();
            throw new LinkingException(this,
                    "fail to connect " + getRemoteAddress() + " from " + url.toInetSocketAddress() + " because "
                            + throwable.getMessage());
        }

        try {
            connect();
        } catch (LinkingException e) {
            // do noting but continue to reconnect
            log.warn(e.getMessage(), e);
        } catch (Throwable throwable) {
            close();
            throw new LinkingException(this,
                    "fail to connect " + getRemoteAddress() + " from " + url.toInetSocketAddress() + " because "
                            + throwable.getMessage());
        }
        RECONNECT_COUNT.set(0);
    }

    private void connect() throws LinkingException {
        CONNECT_LOCK.lock();
        try {
            if (isConnected()) {
                return;
            }
            initReconnectTask();
            doConnect();
            if (!isConnected()) {
                throw new LinkingException(this,
                        "fail to connect " + getRemoteAddress() + " from " + NetUtils.getLocalAddress());
            }
        } catch (Throwable throwable) {
            throw new LinkingException(this,
                    "fail to connect " + getRemoteAddress() + " from " + NetUtils.getLocalAddress() + " because "
                            + throwable.getMessage());
        } finally {
            CONNECT_LOCK.unlock();
        }
    }

    private void disconnect() {
        CONNECT_LOCK.lock();
        try {
            stopReconnectTask();
            try {
                ChannelChain channelChain = getChannelChain();
                if (channelChain != null) {
                    channelChain.close();
                }
            } catch (Throwable e) {
                log.warn(e.getMessage(), e);
            }
            try {
                doDisConnect();
            } catch (Throwable e) {
                log.warn(e.getMessage(), e);
            }
        } finally {
            CONNECT_LOCK.unlock();
        }
    }

    // reconnect task
    private void initReconnectTask() {
        int reconnectCount = getReconnectParam(getUrl());
        if (reconnectCount > 0 && (reconnectExecutorFuture == null || reconnectExecutorFuture.isCancelled())) {
            Runnable task = () -> {
                try {
                    if (!isConnected()) {
                        connect();
                    }
                } catch (Throwable t) {
                    log.warn(NetUtils.getLocalAddress() + " reconnect to " + getRemoteAddress() + " count " + RECONNECT_COUNT.getAndIncrement());
                    log.error("fail to reconnect to " + getRemoteAddress() + " from " + NetUtils.getLocalAddress(), t);
                }
            };
            reconnectExecutorFuture = reconnectExecutorService.scheduleWithFixedDelay(task, reconnectCount, reconnectCount, TimeUnit.MILLISECONDS);
        }
    }

    private synchronized void stopReconnectTask() {
        try {
            if (reconnectExecutorFuture != null && !reconnectExecutorFuture.isDone()) {
                reconnectExecutorFuture.cancel(true);
                reconnectExecutorService.purge();
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }

    private static int getReconnectParam(URL url) {
        int reconnect;
        String param = url.getParam(Constants.PARAMETER.RECONNECT_KEY);
        if (param == null || param.length() == 0 || "true".equalsIgnoreCase(param)) {
            reconnect = Constants.VALUE.DEFAULT_RECONNECT_PERIOD;
        } else if ("false".equalsIgnoreCase(param)) {
            reconnect = 0;
        } else {
            try {
                reconnect = Integer.parseInt(param);
            } catch (Exception e) {
                throw new IllegalArgumentException("reconnect param must be nonnegative integer or false/true. input is:" + param);
            }
            if (reconnect < 0) {
                throw new IllegalArgumentException("reconnect param must be nonnegative integer or false/true. input is:" + param);
            }
        }
        return reconnect;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        }
        return channelChain.getLocalAddress();
    }

    @Override
    public boolean isConnected() {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return false;
        }
        return channelChain.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return false;
        }
        return channelChain.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return null;
        }
        return channelChain.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return;
        }
        channelChain.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return;
        }
        channelChain.removeAttribute(key);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null) {
            return getUrl().toInetSocketAddress();

        }
        return channelChain.getRemoteAddress();
    }

    @Override
    public void send(Object message) throws LinkingException {
        send(message, false);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        if (!isConnected()) {
            connect();
        }
        ChannelChain channelChain = getChannelChain();
        if (channelChain == null || !channelChain.isConnected()) {
            throw new LinkingException(this, "message can not send, because channelChain is closed . url:" + getUrl());
        }
        channelChain.send(message, sent);
    }

    @Override
    public void reset(URL url) {

    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            disconnect();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
        try {
            doClose();
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void reconnect() throws LinkingException {
        if (!isConnected()) {
            CONNECT_LOCK.lock();
            try {
                if (!isConnected()) {
                    disconnect();
                    connect();
                }
            } finally {
                CONNECT_LOCK.unlock();
            }
        }
    }

    /**
     * open client.
     */
    protected abstract void doOen() throws Throwable;

    /**
     * close client.
     */
    protected abstract void doClose() throws Throwable;

    /**
     * connect to the server.
     */
    protected abstract void doConnect() throws Throwable;

    /**
     * disConnect to the server.
     */
    protected abstract void doDisConnect() throws Throwable;

    /**
     * get the current connecting channelChain
     */
    protected abstract ChannelChain getChannelChain();
}
