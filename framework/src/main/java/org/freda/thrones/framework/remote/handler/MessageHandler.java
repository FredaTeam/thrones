package org.freda.thrones.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;

import java.util.concurrent.ExecutorService;

/**
 * Create on 2018/9/19 16:51
 */
@Slf4j
public class MessageHandler extends WrappedChannelChainHandler {

    public MessageHandler(ChannelChainHandler handler, URL url) {
        super(handler, url);
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {
        ExecutorService executorService = getExecutorService();
        try {
            executorService.execute(new ReceiveTask(handler, channelChain, message));
        } catch (Throwable t) {
            throw new LinkingException(channelChain, getClass() + " error when process received event .", t);
        }
    }

    class ReceiveTask implements Runnable {

        private final ChannelChainHandler handler;
        private final ChannelChain channelChain;
        private final Object message;

        ReceiveTask(ChannelChainHandler handler, ChannelChain channelChain, Object message) {
            this.handler = handler;
            this.channelChain = channelChain;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                handler.onReceived(channelChain, message);
            } catch (LinkingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
