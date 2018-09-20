package org.freda.thrones.consumer.invoker;

import org.freda.thrones.framework.msg.ProcedureReqMsg;
import org.freda.thrones.framework.msg.ProcedureRespMsg;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:36
 * @Package org.freda.thrones.consumer.invoker
 * @Describtion
 * contain the template methods for request
 * @file ThronesExecutor.java
 */
public interface ThronesExecutor {

    /**
     * execute the remote call
     * @param reqMsg req msg
     * @return
     */
    public ProcedureRespMsg execute(ProcedureReqMsg reqMsg);

}
