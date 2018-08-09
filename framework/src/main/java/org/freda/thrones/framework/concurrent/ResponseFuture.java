package org.freda.thrones.framework.concurrent;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ResponseFuture
 */
@Slf4j
public class ResponseFuture implements SyncFuture<Object> {

    private static final Map<Long, Channel> CHANNELS = Maps.newConcurrentMap();

    private static final Map<Long, ResponseFuture> FUTURES = Maps.newConcurrentMap();


    /**
     * @see Header#sequence
     */
    private long id;

    private final Channel channel;

    private final ProcedureReqMsg procedureReqMsg;

    private final int timeout;

    public ResponseFuture(Channel channel, ProcedureReqMsg procedureReqMsg, int timeout) {
        this.channel = channel;
        this.procedureReqMsg = procedureReqMsg;
        this.timeout = timeout > 0 ? timeout : Constants.DEFAULT_TIMEOUT;
        this.id = procedureReqMsg.getHeader().getSequence();
        CHANNELS.put(id, channel);
        FUTURES.put(id, this);
    }

    public static ResponseFuture getFuture(long id) {
        return FUTURES.get(id);
    }

    public static boolean hasFuture(Channel channel) {
        return CHANNELS.containsValue(channel);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void setCallBack(FutureCallBack callBack) {

    }
}
