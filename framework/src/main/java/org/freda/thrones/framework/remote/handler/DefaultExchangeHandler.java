package org.freda.thrones.framework.remote.handler;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.exchange.DefaultExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeChannelChain;
import org.freda.thrones.framework.remote.exchange.ExchangeHandler;
import org.freda.thrones.framework.remote.future.ResponseCommonFuture;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Create on 2018/9/8 09:43
 */
@Slf4j
public class DefaultExchangeHandler implements ChannelChainHandlerDelegate {

    private final ExchangeHandler handler;

    public DefaultExchangeHandler(ExchangeHandler handler) {
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");
        this.handler = handler;
    }

    @Override
    public void onConnected(ChannelChain channelChain) throws LinkingException {
        ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onConnected(exchangeChannelChain);
        } finally {
            DefaultExchangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) throws LinkingException {
        ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onDisConnected(exchangeChannelChain);
        } finally {
            DefaultExchangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {
        final ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
        if (message instanceof ProcedureReqMsg) {
            ProcedureReqMsg req = (ProcedureReqMsg) message;
            if (req.getHeader().isTwoWay()) {
                handleRequest(exchangeChannelChain, req);
            } else {
                handler.onReceived(exchangeChannelChain, req.getRequest());
            }
        } else if (message instanceof ProcedureRespMsg) {
            ResponseCommonFuture.receiveRespMsg(channelChain, (ProcedureRespMsg) message);
        } else {
            handler.onReceived(exchangeChannelChain, message);
        }
    }

    private void handleRequest(ExchangeChannelChain exchangeChannelChain, ProcedureReqMsg req) throws LinkingException {
        Header header = new Header(req.getHeader().getSequence());
        ProcedureRespMsg resp = new ProcedureRespMsg(header);

        Object request = req.getRequest();
        try {
            CompletableFuture<Object> future = handler.reply(exchangeChannelChain, request);
            if (future.isDone()) {
                header.setStatus(MsgStatusEnum.SUCCESS);
                resp.setResult(future.get());
                exchangeChannelChain.send(resp);
                return;
            }
            future.whenComplete((result, t) -> {
                try {
                    if (t == null) {
                        header.setStatus(MsgStatusEnum.SUCCESS);
                        resp.setResult(result);
                    } else {
                        header.setStatus(MsgStatusEnum.ERROR);
                        resp.setErrorMsg(t.getMessage());
                    }
                    exchangeChannelChain.send(resp);
                } catch (LinkingException e) {
                    log.warn("Send result to consumer failed, channel is " + exchangeChannelChain + ", msg is " + e);
                }
            });
        } catch (Throwable t) {
            header.setStatus(MsgStatusEnum.ERROR);
            resp.setErrorMsg(t.getMessage());
            exchangeChannelChain.send(resp);
        }
    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) throws LinkingException {
        Throwable exception = null;
        try {
            ExchangeChannelChain exchangeChannelChain = DefaultExchangeChannelChain.getOrAddChannel(channelChain);
            try {
                handler.onSent(exchangeChannelChain, message);
            } finally {
                DefaultExchangeChannelChain.removeChannelIfDisconnected(channelChain);
            }
        } catch (Throwable t) {
            exception = t;
        }
        if (message instanceof ProcedureReqMsg) {
            ProcedureReqMsg reqMsg = (ProcedureReqMsg) message;
            ResponseCommonFuture.sent(reqMsg);
        }
        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else if (exception instanceof LinkingException) {
                throw (LinkingException) exception;
            } else {
                throw new LinkingException(channelChain.getLocalAddress(), channelChain.getRemoteAddress(),
                        exception.getMessage(), exception);
            }
        }
    }

    @Override
    public void onError(ChannelChain channelChain, Throwable throwable) throws LinkingException {

    }

    @Override
    public ChannelChainHandler getHandler() {
        if (handler instanceof ChannelChainHandlerDelegate) {
            return ((ChannelChainHandlerDelegate) handler).getHandler();
        } else {
            return handler;
        }
    }
}
