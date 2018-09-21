package org.freda.thrones.framework.netty4.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.freda.thrones.framework.enums.MsgCommandEnum;
import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.serializer.HessianSerializer;
import org.freda.thrones.framework.serializer.Serializer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Create on 2018/9/21 14:31
 */
public class ThroneCodecTest {

    private static final ThroneCodec codec = new ThroneCodec();

    @Test
    @SuppressWarnings("unchecked")
    public void encode() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ProcedureReqMsg reqMsg = new ProcedureReqMsg();


        Serializer serializer = new HessianSerializer();

        assertEquals(5, serializer.serialize(reqMsg.getRequest()).length);

        codec.encode(null, byteBuf, reqMsg);

        // header + body(null) 22 + 5
        assertEquals(27, byteBuf.readableBytes());
    }

    @Test
    public void decode() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ProcedureReqMsg reqMsg = new ProcedureReqMsg();

        reqMsg.setRequest("request");

        codec.encode(null, byteBuf, reqMsg);

        ProcedureReqMsg newReq = (ProcedureReqMsg) codec.decode(null, byteBuf);

        assertEquals(MsgCommandEnum.PROCEDURE_REQ, newReq.getHeader().getCommand());
        assertEquals("request", newReq.getRequest());

    }

    @Test
    public void test_bytebuf() {
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] header = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        byteBuf.writeBytes(header);

        byte[] body = new byte[]{'b', 'o', 'd', 'y'};
        byteBuf.writeBytes(body);

        assertEquals(14, byteBuf.readableBytes());

        byteBuf.readBytes(13);

        assertEquals('y', (char) byteBuf.readByte());

        assertEquals(0, byteBuf.readableBytes());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_hessian() {

        Serializer serializer = new HessianSerializer();

        ProcedureReqMsg reqMsg = new ProcedureReqMsg();
        byte[] request = serializer.serialize(reqMsg);

        ProcedureReqMsg result = (ProcedureReqMsg) serializer.deserialize(request);
        System.out.println(result.getHeader().getSequence());
    }
}