package org.freda.thrones.framework.remote.handler;

import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.ThronesThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Create on 2018/9/19 16:54
 */
@Slf4j
public class WrappedChannelChainHandler implements ChannelChainHandlerDelegate {

    protected static final ExecutorService SUPPORT_EXECUTOR = Executors.newCachedThreadPool(new ThronesThreadFactory("supportExecutor", true));

    //the thread pool is only to deal message
    protected final ExecutorService executor;

    protected final ChannelChainHandler handler;

    protected final URL url;

    public WrappedChannelChainHandler(ChannelChainHandler handler, URL url) {
        this.executor =
                new ThreadPoolExecutor(Constants.VALUE.MESSAGE_EXECUTOR_THREADS,
                        Constants.VALUE.MESSAGE_EXECUTOR_THREADS,
                        0,
                        TimeUnit.MILLISECONDS,
                        new SynchronousQueue<Runnable>(),
                        new ThronesThreadFactory(Constants.PARAMETER.MESSAGE_EXECUTOR, true),
                        (r, e) -> {
                            String msg = String.format("Thread pool is EXHAUSTED!" +
                                            " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                                            " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s://%s:%d!",
                                    Constants.PARAMETER.MESSAGE_EXECUTOR, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                                    e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating(),
                                    url.getProtocol(), url.getIp(), url.getPort());
                            log.warn(msg);
                            throw new RejectedExecutionException(msg);
                        });
        ;
        this.handler = handler;
        this.url = url;
    }

    @Override
    public ChannelChainHandler getHandler() {
        if (handler instanceof ChannelChainHandlerDelegate) {
            return ((ChannelChainHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }

    @Override
    public void onConnected(ChannelChain channelChain) throws LinkingException {
        handler.onConnected(channelChain);
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) throws LinkingException {
        handler.onDisConnected(channelChain);
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {
        handler.onReceived(channelChain, message);
    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) throws LinkingException {
        handler.onSent(channelChain, message);
    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) throws LinkingException {
        handler.onError(channelChain, throwable);
    }

    public ExecutorService getExecutorService() {
        ExecutorService executorService = executor;
        if (executorService == null || executorService.isShutdown()) {
            executorService = SUPPORT_EXECUTOR;
        }
        return executorService;
    }
}
