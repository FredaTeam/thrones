package org.freda.thrones.framework.concurrent;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.constants.enums.MsgStatusEnum;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ResponseFuture
 */
@Slf4j
public class ResponseFuture implements SyncFuture<Object> {

    private static final Map<Long, ResponseFuture> FUTURES = Maps.newConcurrentMap();

    static {
        Thread compensateTask = new Thread(new TimeoutCallCompensateTask(), "compensateTask");
        compensateTask.setDaemon(true);
        compensateTask.start();
    }
    /**
     * @see Header#sequence
     */
    private long id;

    // communication channel
    private final Channel channel;
    // request
    private final ProcedureReqMsg procedureReqMsg;
    // timeout of client calling
    private final int timeout;
    // response
    private ProcedureRespMsg procedureRespMsg;
    // callback
    private FutureCallBack futureCallBack;
    // current time
    private final long start = System.currentTimeMillis();

    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();

    public ResponseFuture(Channel channel, ProcedureReqMsg procedureReqMsg, int timeout) {
        this.channel = channel;
        this.procedureReqMsg = procedureReqMsg;
        this.timeout = timeout > 0 ? timeout : Constants.DEFAULT_TIMEOUT;
        this.id = procedureReqMsg.getHeader().getSequence();
        FUTURES.put(id, this);
    }

    public static ResponseFuture getFuture(long id) {
        return FUTURES.get(id);
    }

    // client receive response and notify future to get
    public static void receiveRespMsg(ProcedureRespMsg respMsg) {
        ResponseFuture future = FUTURES.remove(respMsg.getHeader().getSequence());
        if (Objects.nonNull(future)) {

        }
    }

    private void doReceive(ProcedureRespMsg respMsg) {
        lock.lock();
        try {
            procedureRespMsg = respMsg;
            done.signal();
        } finally {
            lock.unlock();
        }
        if (Objects.nonNull(futureCallBack)) {
            //todo
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {

        // todo
        procedureRespMsg = new ProcedureRespMsg();
        FUTURES.remove(id);
        return Boolean.TRUE;
    }

    @Override
    public boolean isCancelled() {
        return FUTURES.containsKey(id);
    }

    @Override
    public boolean isDone() {
        return Objects.nonNull(procedureRespMsg);
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            return get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = timeout >= 0 ? timeout : Constants.DEFAULT_TIMEOUT;
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                // this way can cut down time before receive signal
                while (!isDone()) {
                    done.await(timeout, Objects.nonNull(unit) ? unit : TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } finally {
                lock.unlock();
            }
            if (!isDone()) {
                throw new TimeoutException("calling timeout");
            }
        }
        return fetchResponseMsg();
    }

    // todo get object from response
    private Object fetchResponseMsg() throws TimeoutException {
        ProcedureRespMsg respMsg = procedureRespMsg;
        if (Objects.isNull(respMsg)) {
            throw new IllegalStateException("response is null");
        }
        MsgStatusEnum status = respMsg.getHeader().getStatus();
        if (MsgStatusEnum.SUCCESS == status) {
            return respMsg;
        }
        if (MsgStatusEnum.TIMEOUT == status) {
            throw new TimeoutException("calling timeout");
        }
        throw new RuntimeException("fetch response error");
    }

    @Override
    public void setCallBack(FutureCallBack callBack) {
        if (isDone()) {
            executeCallBack(callBack);
        } else {

        }
    }

    private void executeCallBack(FutureCallBack callBack) {
    }

    // timeout compensate
    private static class TimeoutCallCompensateTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    for (ResponseFuture future : FUTURES.values()) {
                        if (Objects.isNull(future) || future.isDone()) {
                            continue;
                        }
                        if (System.currentTimeMillis() - future.start > future.timeout) {

                        }
                    }
                } catch (Exception e) {
                    log.error("timeoutCallCompensateTask error", e);
                }
            }
        }
    }
}
