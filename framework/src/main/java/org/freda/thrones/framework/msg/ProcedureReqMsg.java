package org.freda.thrones.framework.msg;

public class ProcedureReqMsg extends BaseMsg {

    public ProcedureReqMsg(Header header, byte[] bodyBytes) {

        super(header, bodyBytes);
    }

    public ProcedureReqMsg() {
    }

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

    }

    public Class<?>[] getArgsTypes() {
        return argsTypes;
    }

    public void setArgsTypes(Class<?>[] argsTypes) {
        this.argsTypes = argsTypes;
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
}
