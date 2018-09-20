package org.freda.thrones.framework.exceptions;

import org.freda.thrones.framework.remote.ChannelChain;

public class TimeoutException extends LinkingException {


    public TimeoutException(ChannelChain channelChain, String message) {
        super(channelChain, message);
    }
}
