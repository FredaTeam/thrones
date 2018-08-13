package org.freda.thrones.framework.remote.future;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.exceptions.TimeoutException;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.remote.ChannelChain;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ResponseCommonFuture
 */
@Slf4j
public class ResponseCommonFuture implements CommonFuture {

    private static final Map<Long, ResponseCommonFuture> FUTURES = Maps.newConcurrentMap();

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
    private final ChannelChain channelChain;
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

    public ResponseCommonFuture(ChannelChain channelChain, ProcedureReqMsg procedureReqMsg, int timeout) {
        this.channelChain = channelChain;
        this.procedureReqMsg = procedureReqMsg;
        this.timeout = timeout > 0 ? timeout : Constants.DEFAULT_TIMEOUT;
        this.id = procedureReqMsg.getHeader().getSequence();
        FUTURES.put(id, this);
    }

    public static ResponseCommonFuture getFuture(long id) {
        return FUTURES.get(id);
    }

    // client receive response and notify future to get
    public static void receiveRespMsg(ProcedureRespMsg respMsg) {
        ResponseCommonFuture future = FUTURES.remove(respMsg.getHeader().getSequence());
        if (Objects.nonNull(future)) {
            future.doReceive(respMsg);
        } else {
            log.warn("return response timeout");
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
            executeCallBack(futureCallBack);
        }
    }

    @Override
    public boolean cancel() {
        procedureRespMsg = new ProcedureRespMsg(
                MsgStatusEnum.TIMEOUT,
                id,
                "calling timeout",
                null,
                false
        );
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
    public Object get() throws LinkingException {
        try {
            return get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws LinkingException {
        timeout = timeout >= 0 ? timeout : Constants.DEFAULT_TIMEOUT;
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                // this way can reduce time before receive signal
                while (!isDone()) {
                    done.await(timeout, Objects.nonNull(unit) ? unit : TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (!isDone()) {
                throw new TimeoutException(channelChain, "calling timeout");
            }
        }
        return fetchResponseMsg();
    }

    private Object fetchResponseMsg() throws TimeoutException {
        ProcedureRespMsg respMsg = procedureRespMsg;
        if (Objects.isNull(respMsg)) {
            throw new IllegalStateException("response is null");
        }
        MsgStatusEnum status = respMsg.getHeader().getStatus();
        if (MsgStatusEnum.SUCCESS == status) {
            return respMsg.getResult();
        }
        if (MsgStatusEnum.TIMEOUT == status) {
            throw new TimeoutException(channelChain, "calling timeout");
        }
        throw new RuntimeException("fetch response error");
    }

    @Override
    public void setCallBack(FutureCallBack callBack) {
        if (isDone()) {
            executeCallBack(callBack);
        } else {
            boolean isDone = false;
            lock.lock();
            try {
                if (!isDone()) {
                    this.futureCallBack = callBack;
                } else {
                    isDone = true;
                }
            } finally {
                lock.unlock();
            }
            if (isDone) {
                executeCallBack(callBack);
            }
        }
    }

    private void executeCallBack(FutureCallBack callBack) {
        FutureCallBack futureCallBack = callBack;
        if (Objects.isNull(futureCallBack)) {
            throw new NullPointerException("callback can not be null");
        }
        // for gc
        callBack = null;
        ProcedureRespMsg res = procedureRespMsg;
        if (MsgStatusEnum.SUCCESS == res.getHeader().getStatus()) {
            try {
                futureCallBack.success(res);
            } catch (Exception e) {
                log.error("callback execute error,res: " + res.getResult(), e);
            }
        } else if (MsgStatusEnum.TIMEOUT == res.getHeader().getStatus()) {
            try {
                TimeoutException exception = new TimeoutException(channelChain, res.getErrorMsg());
                futureCallBack.failure(exception);
            } catch (Exception e) {
                log.error("callback execute error,res: " + res.getResult(), e);
            }
        } else {
            try {
                RuntimeException exception = new RuntimeException(res.getErrorMsg());
                futureCallBack.failure(exception);
            } catch (Exception e) {
                log.error("callback execute error,res: " + res.getResult(), e);
            }
        }

    }

    // timeout compensate
    private static class TimeoutCallCompensateTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    for (ResponseCommonFuture future : FUTURES.values()) {
                        if (Objects.isNull(future) || future.isDone()) {
                            continue;
                        }
                        if (System.currentTimeMillis() - future.start > future.timeout) {
                            ProcedureRespMsg respMsg = new ProcedureRespMsg(
                                    MsgStatusEnum.TIMEOUT,
                                    future.id,
                                    "calling timeout",
                                    null,
                                    false
                            );
                            receiveRespMsg(respMsg);
                        }
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                } catch (Exception e) {
                    log.error("timeoutCallCompensateTask error", e);
                }
            }
        }
    }

}
