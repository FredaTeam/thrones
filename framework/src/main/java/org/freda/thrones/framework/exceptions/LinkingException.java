package org.freda.thrones.framework.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.freda.thrones.framework.remote.ChannelChain;

import java.net.InetSocketAddress;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkingException extends Exception {

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    public LinkingException(ChannelChain channelChain, String message) {
        this(
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                message
        );
    }

    public LinkingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public LinkingException(ChannelChain channelChain, Throwable cause) {
        this(
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                cause
        );
    }

    public LinkingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Throwable cause) {
        super(cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public LinkingException(ChannelChain channelChain, String message, Throwable cause) {
        this(
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                Objects.isNull(channelChain) ? null : channelChain.getLocalAddress(),
                message,
                cause
        );
    }

    public LinkingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message, Throwable cause) {
        super(message, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }
}
