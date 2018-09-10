package org.freda.thrones.framework.netty4;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;
import org.freda.thrones.framework.remote.exchange.AbstractChannelChain;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Netty4ChannelChain extends AbstractChannelChain {

    private static final ConcurrentMap<Channel, Netty4ChannelChain> channelMap = new ConcurrentHashMap<>();

    private final Channel channel;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();


    private Netty4ChannelChain(Channel channel, URL url, ChannelChainHandler handler){
        super(url, handler);

        if (channel == null){
            throw new IllegalArgumentException("netty channel == null;");
        }
        this.channel = channel;
    }

    /**
     * if netty channel is active
     * return this. in MAP
     *
     * @param channel netty channel
     * @param url URL
     * @param handler handler
     * @return this
     */
    static Netty4ChannelChain getOrAddChannel(Channel channel, URL url, ChannelChainHandler handler){

        if (channel == null){
            return null;
        }
        Netty4ChannelChain chain = channelMap.get(channel);
        if (chain == null){

            Netty4ChannelChain netty4ChannelChain = new Netty4ChannelChain(channel, url, handler);

            if (channel.isActive()){
                chain = channelMap.putIfAbsent(channel, netty4ChannelChain);
            }
            if (chain == null){
                chain = netty4ChannelChain;
            }
        }
        return chain;
    }

    /**
     * remove the is not active channel.
     *
     * @param channel
     */
    static void removeChannelIfDisconnected(Channel channel) {
        if (channel != null && !channel.isActive()){
            channelMap.remove(channel);
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isConnected() {
        return closed() && !channel.isActive();
    }

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        if (value == null){
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public void closing() {
        super.closing();
        if (isClosing()){

            removeChannelIfDisconnected(channel);
            attributes.clear();
            channel.close();
        }
        setClosing(false);
    }

    @Override
    public void send(Object message, boolean sent) throws LinkingException {
        boolean success = true;
        int timeout = 5000;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (sent) {
                //timeout = getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
                success = future.await(timeout);
            }
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable e) {
            throw new LinkingException(this, "Failed to send message " + message + " to " + getRemoteAddress() + ", cause: " + e.getMessage(), e);
        }

        if (!success) {
            throw new LinkingException(this, "Failed to send message " + message + " to " + getRemoteAddress()
                    + "in timeout(" + timeout + "ms) limit");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Netty4ChannelChain that = (Netty4ChannelChain) o;
        return Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channel == null) ? 0 : channel.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Netty4ChannelChain{" +
                "channel=" + channel +
                ", attributes=" + attributes +
                '}';
    }
}
