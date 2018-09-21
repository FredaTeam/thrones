package org.freda.thrones.framework.remote.client;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.exechange.DefaultExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeHandler;
import org.freda.thrones.framework.remote.future.CommonFuture;

import java.net.InetSocketAddress;

/**
 * Create on 2018/8/18 14:15
 */
public class DefaultExechangeClient implements ExechangeClient {

    private final Client client;

    private final ExechangeChannelChain channelChain;

    // todo need to complete heartbeat task
    public DefaultExechangeClient(Client client, boolean needHeartbeat) {
        this.client = client;
        this.channelChain = new DefaultExechangeChannelChain(client);

        if (needHeartbeat) {
            startHeartBeatTask();
        }
    }

    private void startHeartBeatTask() {

    }

    private void stopHeartBeatTask() {

    }

    @Override
    public void reconnect() throws LinkingException {
        client.reconnect();
    }

    @Override
    public CommonFuture request(Object request) throws LinkingException {
        return channelChain.request(request);
    }

    @Override
    public CommonFuture request(Object request, int timeout) throws LinkingException {
        return channelChain.request(request, timeout);
    }

    @Override
    public ExechangeHandler getExechangeHandler() {
        return channelChain.getExechangeHandler();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return client.getLocalAddress();
    }

    @Override
    public boolean isConnected() {
        return channelChain.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return channelChain.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {
        channelChain.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        channelChain.removeAttribute(key);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channelChain.getRemoteAddress();
    }

    @Override
    public URL getUrl() {
        return client.getUrl();
    }

    @Override
    public void send(Object message) throws LinkingException {
        channelChain.send(message);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        channelChain.send(message, sent);
    }

    @Override
    public void reset(URL url) {
        client.reset(url);
    }

    @Override
    public void close() {
        stopHeartBeatTask();
        channelChain.close();
    }

    @Override
    public void close(int timeout) {
        closing();
    }

    @Override
    public void closing() {
        channelChain.closing();
        stopHeartBeatTask();
        channelChain.close();
    }

    @Override
    public boolean closed() {
        return channelChain.closed();
    }
}
