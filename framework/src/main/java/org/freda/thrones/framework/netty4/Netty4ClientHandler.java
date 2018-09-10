package org.freda.thrones.framework.netty4;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.remote.handler.ChannelChainHandler;

@ChannelHandler.Sharable
public class Netty4ClientHandler extends ChannelDuplexHandler {

    private final URL url;

    private final ChannelChainHandler channelChainHandler;

    public Netty4ClientHandler(URL url, ChannelChainHandler channelChainHandler) {

        if (url == null || channelChainHandler == null){
            throw new IllegalArgumentException("url or channelChainHandler can not be null.");
        }
        this.channelChainHandler = channelChainHandler;
        this.url = url;
    }

    /**
     * if connected.
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(),this.url, this.channelChainHandler);
        try {
            channelChainHandler.onConnected(channelChain);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }

    }

    /**
     * if disconnected.
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(),this.url, this.channelChainHandler);
        try {
            channelChainHandler.onDisConnected(channelChain);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * if receive msg.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(),this.url, this.channelChainHandler);
        try {
            channelChainHandler.onReceived(channelChain, msg);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(), this.url, this.channelChainHandler);

        try {
            //if write req error.
            //then return error resp
            if (promise != null && msg instanceof ProcedureReqMsg){
                ProcedureReqMsg reqMsg = (ProcedureReqMsg)msg;
                ProcedureRespMsg respMsg = new ProcedureRespMsg(MsgStatusEnum.ERROR,
                        reqMsg.getHeader().getSequence(),
                        promise.cause().getMessage(), null, Boolean.FALSE);
                channelChainHandler.onReceived(channelChain, respMsg);
            } else {
                channelChainHandler.onSent(channelChain, msg);
            }
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Netty4ChannelChain channelChain = Netty4ChannelChain.getOrAddChannel(ctx.channel(),this.url, this.channelChainHandler);
        try {
            channelChainHandler.onError(channelChain, cause);
        } finally {
            Netty4ChannelChain.removeChannelIfDisconnected(ctx.channel());
        }
    }
}
