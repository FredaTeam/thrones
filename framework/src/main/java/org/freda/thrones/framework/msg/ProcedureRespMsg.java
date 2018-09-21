package org.freda.thrones.framework.msg;

import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.enums.MsgCommandEnum;
import org.freda.thrones.framework.enums.MsgStatusEnum;
import org.freda.thrones.framework.serializer.SerializerFactory;
import org.freda.thrones.framework.utils.NumberBytesConvertUtils;

public class ProcedureRespMsg extends BaseMsg {


    public ProcedureRespMsg() {
    }

    /**
     * 用于手动创建Resp
     * <p>
     * ************
     * if returnVoid is true that set the result is null please.
     * ************
     *
     * @param statusEnum 状态
     * @param errorMsg
     * @param result
     * @param returnVoid
     */
    public ProcedureRespMsg(MsgStatusEnum statusEnum, Long sequence, String errorMsg, Object result, boolean returnVoid) {

        this.header = new Header(MsgCommandEnum.PROCEDURE_RES, statusEnum, sequence);

        this.errorMsg = errorMsg;

        this.result = result;

        this.returnVoid = returnVoid;


    }

    public ProcedureRespMsg(Header header, byte[] bodyBytes) {

        super(header, bodyBytes);
    }

    public ProcedureRespMsg(Header header) {
        this.header = header;
    }

    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 返回结果
     */
    private Object result;
    /**
     * 是否为void
     */
    private boolean returnVoid;

    /**
     * read bytes to msg.
     */
    @Override
    protected void bytesToMsg() {
        int pos = 1;

        returnVoid = bodyBytes[0] == (byte) 1;


        byte[] temp = new byte[4];
        System.arraycopy(bodyBytes, pos, temp, 0, 4);
        int errorMsgLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        if (errorMsgLen > 0) {
            temp = new byte[errorMsgLen];
            System.arraycopy(bodyBytes, pos, temp, 0, errorMsgLen);
            errorMsg = new String(temp);
            pos += errorMsgLen;
        }

        temp = new byte[4];
        System.arraycopy(bodyBytes, pos, temp, 0, 4);
        int resultLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        temp = new byte[resultLen];
        System.arraycopy(bodyBytes, pos, temp, 0, resultLen);
        result = SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).deserialize(temp);
    }

    /**
     * bean to bodyBytes
     */
    @Override
    protected void msgToBytes() {
        byte[] errorMsgByte = (errorMsg != null && !errorMsg.equals("") ? errorMsg.getBytes() : new byte[0]);
        byte[] resultByte = SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).serialize(result);
        int pos = 1;

        bodyBytes = new byte[1 + 2 * 4 + errorMsgByte.length + resultByte.length];

        bodyBytes[0] = returnVoid ? (byte) 1 : (byte) 0;

        System.arraycopy(NumberBytesConvertUtils.intToBytes4(errorMsgByte.length), 0, bodyBytes, pos, 4);
        pos += 4;
        if (errorMsgByte.length > 0) {
            System.arraycopy(errorMsgByte, 0, bodyBytes, pos, errorMsgByte.length);
            pos += errorMsgByte.length;
        }
        System.arraycopy(NumberBytesConvertUtils.intToBytes4(resultByte.length), 0, bodyBytes, pos, 4);
        pos += 4;
        System.arraycopy(resultByte, 0, bodyBytes, pos, resultByte.length);

    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isReturnNotNull() {
        return returnVoid;
    }

    public void setReturnNotNull(boolean returnVoid) {
        this.returnVoid = returnVoid;
    }
}
