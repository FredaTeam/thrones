package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.enums.MsgCommandEnum;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.exceptions.LinkingException;
import org.freda.thrones.framework.msg.Header;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;
import org.freda.thrones.framework.netty4.Netty4ChannelChain;
import org.freda.thrones.framework.remote.ChannelChain;
import org.freda.thrones.framework.serializer.HessianSerializer;
import org.freda.thrones.framework.utils.NumberBytesConvertUtils;

import java.util.List;

/**
 * Create on 2018/9/20 11:38
 */
@Slf4j
public class ThroneCodec extends AbstractCodec {

    /**
     * header define as follows
     * <p>
     * magic + req/res + status + twoWay + sequence + totalLength
     * <p>
     * 7 + 1 + 1 + 1 + 8 + 4 = 22
     */
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

        Header header = req.getHeader();
        // header totallenth
        int headerLenth = ThronesTCPConstant.THRONES_MSG_HEADER_LEN + body.length;

        // write header bytes
        byte[] headerBytes = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN];

        if (StringUtils.isBlank(header.getMagic())) {

            System.arraycopy(ThronesTCPConstant.THRONES_TCP_MAGIC.getBytes(), 0, headerBytes, 0, 7);

        }

        headerBytes[7] = MsgCommandEnum.PROCEDURE_REQ.getCommand();
        headerBytes[9] = (byte) (header.isTwoWay() ? TWO_WAY : ONE_WAY);
        NumberBytesConvertUtils.long2bytes(header.getSequence(), headerBytes, 10);
        NumberBytesConvertUtils.int2bytes(headerLenth, headerBytes, 18);

        // write header
        byteBuf.writeBytes(headerBytes);

        // write body
        byteBuf.writeBytes(body);
    }

    private void encodeResp(Netty4ChannelChain channelChain, ByteBuf byteBuf, ProcedureRespMsg resp) {
        // serialize req body
        HessianSerializer serializer = new HessianSerializer();
        String errorMsg = resp.getErrorMsg();
        Object result = resp.getResult();

        byte[] body = serializer.serialize(result);

        Header header = resp.getHeader();
        // header totallenth
        int headerLenth = ThronesTCPConstant.THRONES_MSG_HEADER_LEN + body.length;

        // write header bytes
        byte[] headerBytes = new byte[ThronesTCPConstant.THRONES_MSG_HEADER_LEN];

        if (StringUtils.isBlank(header.getMagic())) {

            System.arraycopy(ThronesTCPConstant.THRONES_TCP_MAGIC.getBytes(), 0, headerBytes, 0, 7);

        }

        headerBytes[7] = MsgCommandEnum.PROCEDURE_RES.getCommand();
        headerBytes[8] = StringUtils.isBlank(errorMsg) ? MsgStatusEnum.SUCCESS.getStatus() : MsgStatusEnum.ERROR.getStatus();
        headerBytes[9] = (byte) (header.isTwoWay() ? 0 : 1);
        NumberBytesConvertUtils.long2bytes(header.getSequence(), headerBytes, 10);
        NumberBytesConvertUtils.int2bytes(headerLenth, headerBytes, 18);

        byteBuf.writeBytes(headerBytes);

        byteBuf.writeBytes(body);
    }

    @Override
    public Object decode(Netty4ChannelChain channelChain, ByteBuf byteBuf) {
        int readable = byteBuf.readableBytes();
        byte[] header = new byte[Math.min(readable, ThronesTCPConstant.THRONES_MSG_HEADER_LEN)];
        byteBuf.readBytes(header);
        byte command = header[7];
        if (MsgCommandEnum.PROCEDURE_REQ.getCommand() == command) {
            return decodeReq(channelChain, byteBuf, header);
        } else {
            return decodeResp(channelChain, byteBuf, header);
        }
    }

    private ProcedureRespMsg decodeResp(Netty4ChannelChain channelChain, ByteBuf byteBuf, byte[] header) {
        return null;
    }

    private ProcedureReqMsg decodeReq(Netty4ChannelChain channelChain, ByteBuf byteBuf, byte[] header) {

        return null;
    }
}
