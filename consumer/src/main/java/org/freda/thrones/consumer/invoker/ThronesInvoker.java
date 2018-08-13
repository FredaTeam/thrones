package org.freda.thrones.consumer.invoker;

import org.freda.thrones.consumer.ThronesService;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午5:04
 * @Package org.freda.thrones.consumer.invoker
 * @Describtion
 * an annotation class will connect to a invoker
 * @file ThronesInvoker.java
 */
public interface ThronesInvoker {

    /**
     * invoke remote procedure call with the service entity
     *
     * @param service service enetity
     */
    public void invoke(ThronesService service);
}
