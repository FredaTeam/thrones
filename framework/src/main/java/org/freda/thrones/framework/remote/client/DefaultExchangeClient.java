package org.freda.thrones.framework.remote.client;

import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.exchange.ExchangeClient;
import org.freda.thrones.framework.remote.exchange.ExchangeHandler;
import org.freda.thrones.framework.remote.future.CommonFuture;

import java.net.InetSocketAddress;
import java.net.URL;

/**
 * Create on 2018/8/18 14:15
 */
public class DefaultExchangeClient implements ExchangeClient {



    @Override
    public void reconnect() throws LinkingException {

    }

    @Override
    public CommonFuture request(Object request) throws LinkingException {
        return null;
    }

    @Override
    public CommonFuture request(Object request, int timeout) throws LinkingException {
        return null;
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean hasAttribute(String key) {
        return false;
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public void removeAttribute(String key) {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public void send(Object message) {

    }

    @Override
    public void send(Object message, boolean sent) {

    }

    @Override
    public void reset(URL url) {

    }

    @Override
    public void close() {

    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public void closing() {

    }

    @Override
    public boolean closed() {
        return false;
    }
}
