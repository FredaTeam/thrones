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

import java.io.IOException;
import java.util.List;

/**
 * Create on 2018/9/20 11:38
 * <p>
 * todo fix me please ~.~
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
    public void encode(Netty4ChannelChain channelChain, ByteBuf byteBuf, Object msg) throws IOException {
        if (msg instanceof ProcedureReqMsg) {

            encodeReq(channelChain, byteBuf, (ProcedureReqMsg) msg);

        } else if (msg instanceof ProcedureRespMsg) {

            encodeResp(channelChain, byteBuf, (ProcedureRespMsg) msg);

        } else {

            log.warn("unknown msg type");
            throw new IOException("unknown msg type");

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
        headerBytes[9] = (byte) (header.isTwoWay() ? TWO_WAY : ONE_WAY);
        NumberBytesConvertUtils.long2bytes(header.getSequence(), headerBytes, 10);
        NumberBytesConvertUtils.int2bytes(headerLenth, headerBytes, 18);

        byteBuf.writeBytes(headerBytes);

        byteBuf.writeBytes(body);
    }

    @Override
    public Object decode(Netty4ChannelChain channelChain, ByteBuf byteBuf) throws IOException {
        int readable = byteBuf.readableBytes();
        byte[] header = new byte[Math.min(readable, ThronesTCPConstant.THRONES_MSG_HEADER_LEN)];
        byteBuf.readBytes(header);
        byte command = header[7];
        if (MsgCommandEnum.PROCEDURE_REQ.getCommand() == command) {

            return decodeReq(channelChain, byteBuf, header);

        }
        if (MsgCommandEnum.PROCEDURE_RES.getCommand() == command) {

            return decodeResp(channelChain, byteBuf, header);

        }
        log.warn("unknown command type");
        throw new IOException("unknown command type");
    }

    private ProcedureRespMsg decodeResp(Netty4ChannelChain channelChain, ByteBuf byteBuf, byte[] headerBytes) {
        Header header = new Header();
        header.setStatus(MsgStatusEnum.statusOf(headerBytes[8]));
        header.setSequence(NumberBytesConvertUtils.bytes2long(headerBytes, 10));
        header.setTwoWay(headerBytes[9] == TWO_WAY);

        int bodyLength = NumberBytesConvertUtils.bytes2int(headerBytes, 18) - ThronesTCPConstant.THRONES_MSG_HEADER_LEN;

        byte[] body = new byte[bodyLength];
        byteBuf.readBytes(body);
        HessianSerializer serializer = new HessianSerializer();
        Object result = serializer.deserialize(body);

        ProcedureRespMsg respMsg = new ProcedureRespMsg(header);
        respMsg.setResult(result);
        return respMsg;
    }

    private ProcedureReqMsg decodeReq(Netty4ChannelChain channelChain, ByteBuf byteBuf, byte[] headerBytes) {

        Header header = new Header();
        header.setCommand(MsgCommandEnum.commandOf(headerBytes[7]));
        header.setSequence(NumberBytesConvertUtils.bytes2long(headerBytes, 10));
        header.setTwoWay(headerBytes[9] == TWO_WAY);

        int bodyLength = NumberBytesConvertUtils.bytes2int(headerBytes, 18) - ThronesTCPConstant.THRONES_MSG_HEADER_LEN;

        byte[] body = new byte[bodyLength];
        byteBuf.readBytes(body);
        HessianSerializer serializer = new HessianSerializer();
        Object request = serializer.deserialize(body);

        ProcedureReqMsg reqMsg = new ProcedureReqMsg(header);
        reqMsg.setRequest(request);
        return reqMsg;
    }
}
