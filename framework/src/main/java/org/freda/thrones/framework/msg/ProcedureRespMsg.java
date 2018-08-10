package org.freda.thrones.framework.msg;

import org.freda.thrones.framework.enums.MsgCommandEnum;
import org.freda.thrones.framework.enums.MsgStatusEnum;

public class ProcedureRespMsg extends BaseMsg {


    public ProcedureRespMsg() {
    }

    /**
     * 用于手动创建Resp
     *
     * @param statusEnum 状态
     * @param errorMsg
     * @param result
     * @param returnNotNull
     */
    public ProcedureRespMsg(MsgStatusEnum statusEnum, Long sequence, String errorMsg, Object result, boolean returnNotNull) {

        this.header = new Header(MsgCommandEnum.PROCEDURE_RES, statusEnum, sequence);

        this.errorMsg = errorMsg;

        this.result = result;

        this.returnNotNull = returnNotNull;


    }

    public ProcedureRespMsg(Header header, byte[] bodyBytes) {

        super(header, bodyBytes);
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
    private boolean returnNotNull;

    /**
     * read bytes to msg.
     */
    @Override
    protected void bytesToMsg()
    {
        
    }

    /**
     * bean to bodyBytes
     */
    @Override
    protected void msgToBytes()
    {

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
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }
}
