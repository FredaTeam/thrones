package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.netty4.Netty4ChannelChain;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.serializer.HessianSerializer;

/**
 * Create on 2018/9/20 11:38
 */
@Slf4j
public class ThroneCodec extends AbstractCodec {

    @Override
    public void encode(Netty4ChannelChain channelChain, ByteBuf byteBuf, Object msg) {
        if (msg instanceof ProcedureReqMsg) {
            encodeReq(channelChain, byteBuf, (ProcedureReqMsg) msg);
        } else if (msg instanceof ProcedureRespMsg) {
            encodeResp(channelChain, byteBuf, (ProcedureRespMsg) msg);
        } else {
            log.warn("unknow msg type");
        }

    }

    private void encodeReq(Netty4ChannelChain channelChain, ByteBuf byteBuf, ProcedureReqMsg req) {
        // serialize req body
        HessianSerializer serializer = new HessianSerializer();
        Object data = req.getRequest();
        byte[] body = serializer.serialize(data);

        // set req header totallenth
        req.getHeader().setTotalLen(ThronesTCPConstant.THRONES_MSG_HEADER_LEN + body.length);

        // write header bytes
        byte[] header = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN];
        byteBuf.writerIndex(ThronesTCPConstant.THRONES_MSG_HEADER_LEN);
        byteBuf.writeBytes(header);

        byteBuf.writerIndex(body.length);
        byteBuf.writeBytes(body);
    }

    private void encodeResp(Netty4ChannelChain channelChain, ByteBuf byteBuf, ProcedureRespMsg resp) {
        byte[] header = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN];

    }

    @Override
    public Object decode(Netty4ChannelChain channelChain, ByteBuf byteBuf) {

        return null;
    }
}
