package org.freda.thrones.framework.remote.handler;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.remote.exechange.DefaultExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeChannelChain;
import org.freda.thrones.framework.remote.exechange.ExechangeHandler;
import org.freda.thrones.framework.remote.future.ResponseCommonFuture;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Create on 2018/9/8 09:43
 */
@Slf4j
public class DefaultExechangeHandler implements ChannelChainHandlerDelegate {

    private final ExechangeHandler handler;

    public DefaultExechangeHandler(ExechangeHandler handler) {
        Preconditions.checkArgument(Objects.nonNull(handler), "handler can not be null");
        this.handler = handler;
    }

    @Override
    public void onConnected(ChannelChain channelChain) throws LinkingException {
        ExechangeChannelChain exechangeChannelChain = DefaultExechangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onConnected(exechangeChannelChain);
        } finally {
            DefaultExechangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onDisConnected(ChannelChain channelChain) throws LinkingException {
        ExechangeChannelChain exechangeChannelChain = DefaultExechangeChannelChain.getOrAddChannel(channelChain);
        try {
            handler.onDisConnected(exechangeChannelChain);
        } finally {
            DefaultExechangeChannelChain.removeChannelIfDisconnected(channelChain);
        }
    }

    @Override
    public void onReceived(ChannelChain channelChain, Object message) throws LinkingException {
        final ExechangeChannelChain exechangeChannelChain = DefaultExechangeChannelChain.getOrAddChannel(channelChain);
        if (message instanceof ProcedureReqMsg) {
            ProcedureReqMsg req = (ProcedureReqMsg) message;
            if (req.getHeader().isTwoWay()) {
                handleRequest(exechangeChannelChain, req);
            } else {
                handler.onReceived(exechangeChannelChain, req.getRequest());
            }
        } else if (message instanceof ProcedureRespMsg) {
            System.out.println(((ProcedureRespMsg) message).getResult());
            ResponseCommonFuture.receiveRespMsg(channelChain, (ProcedureRespMsg) message);
        } else {
            handler.onReceived(exechangeChannelChain, message);
        }
    }

    private void handleRequest(ExechangeChannelChain exechangeChannelChain, ProcedureReqMsg req) throws LinkingException {
        Header header = new Header(req.getHeader().getSequence());
        ProcedureRespMsg resp = new ProcedureRespMsg(header);

        Object request = req.getRequest();
        try {
            CompletableFuture<Object> future = handler.reply(exechangeChannelChain, request);
            if (future.isDone()) {
                header.setStatus(MsgStatusEnum.SUCCESS);
                resp.setResult(future.get());
                exechangeChannelChain.send(resp);
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
                    exechangeChannelChain.send(resp);
                } catch (LinkingException e) {
                    log.warn("Send result to consumer failed, channel is " + exechangeChannelChain + ", msg is " + e);
                }
            });
        } catch (Throwable t) {
            header.setStatus(MsgStatusEnum.ERROR);
            resp.setErrorMsg(t.getMessage());
            exechangeChannelChain.send(resp);
        }
    }

    @Override
    public void onSent(ChannelChain channelChain, Object message) throws LinkingException {

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
