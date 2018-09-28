package org.freda.thrones.framework.msg;

import org.freda.thrones.framework.constants.ThronesTCPConstant;
import org.freda.thrones.framework.serializer.SerializerFactory;
import org.freda.thrones.common.utils.NumberBytesConvertUtils;

public class ProcedureReqMsg extends BaseMsg {

    public ProcedureReqMsg(Header header, byte[] bodyBytes) {
        super(header, bodyBytes);
    }

    public ProcedureReqMsg(Header header) {
        this.header = header;
    }

    public ProcedureReqMsg() {
    }

    // encapsulation of methodname args argTypes
    private Object request;

    /**
     * 接口class 如 org.freda.api.xxxItf
     */
    private String itfName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] args;
    /**
     * 参数类型
     */
    private Class<?>[] argsTypes;

    /**
     * read bytes to msg.
     */
    @Override
    protected void bytesToMsg() {
        int pos = 0;

        byte[] temp = new byte[4];
        System.arraycopy(bodyBytes, 0, temp, 0, 4);
        int itfNameLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        temp = new byte[itfNameLen];
        System.arraycopy(bodyBytes, pos, temp, 0, itfNameLen);
        this.itfName = new String(temp);
        pos += itfNameLen;

        temp = new byte[4];
        System.arraycopy(bodyBytes, pos, temp, 0, 4);
        int methodNameLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        temp = new byte[methodNameLen];
        System.arraycopy(bodyBytes, pos, temp, 0, methodNameLen);
        this.methodName = new String(temp);
        pos += methodNameLen;

        temp = new byte[4];
        System.arraycopy(bodyBytes, pos, temp, 0, 4);
        int argsLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        temp = new byte[argsLen];
        System.arraycopy(bodyBytes, pos, temp, 0, argsLen);
        this.args = (Object[]) SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).deserialize(temp);
        pos += argsLen;

        temp = new byte[4];
        System.arraycopy(bodyBytes, pos, temp, 0, 4);
        int argsTypesLen = NumberBytesConvertUtils.bytes4ToInt(temp);
        pos += 4;

        temp = new byte[argsTypesLen];
        System.arraycopy(bodyBytes, pos, temp, 0, argsTypesLen);
        this.argsTypes = (Class<?>[]) SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).deserialize(temp);
    }

    /**
     * bean to bodyBytes
     */
    @Override
    protected void msgToBytes() {

        byte[] itfNameBytes = itfName.getBytes();
        byte[] methodNameBytes = methodName.getBytes();
        byte[] argsBytes = SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).serialize(args);
        byte[] argsTypesBytes = SerializerFactory.getSerializer(ThronesTCPConstant.DEFAULT_SERIALIZER).serialize(argsTypes);

        int bodyBytesTotalLen = 4 * 4 + itfNameBytes.length + methodNameBytes.length + argsBytes.length + argsTypesBytes.length;

        super.bodyBytes = new byte[bodyBytesTotalLen];

        int pos = 0;
        System.arraycopy(NumberBytesConvertUtils.intToBytes4(itfNameBytes.length), 0, bodyBytes, pos, 4);
        pos += 4;
        System.arraycopy(itfNameBytes, 0, itfNameBytes, pos, itfNameBytes.length);
        pos += itfNameBytes.length;
        System.arraycopy(NumberBytesConvertUtils.intToBytes4(methodNameBytes.length), 0, bodyBytes, pos, 4);
        pos += 4;
        System.arraycopy(methodNameBytes, 0, bodyBytes, pos, methodNameBytes.length);
        pos += methodNameBytes.length;
        System.arraycopy(NumberBytesConvertUtils.intToBytes4(argsBytes.length), 0, bodyBytes, pos, 4);
        pos += 4;
        System.arraycopy(argsBytes, 0, bodyBytes, pos, argsBytes.length);
        pos += argsBytes.length;
        System.arraycopy(NumberBytesConvertUtils.intToBytes4(argsTypesBytes.length), 0, bodyBytes, pos, 4);
        pos += 4;
        System.arraycopy(argsTypesBytes, 0, bodyBytes, pos, argsTypesBytes.length);

    }

    public Class<?>[] getArgsTypes() {
        return argsTypes;
    }

    public String getItfName() {
        return itfName;
    }

    public void setItfName(String itfName) {
        this.itfName = itfName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }
}
