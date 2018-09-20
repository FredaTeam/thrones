package org.freda.thrones.consumer.blancer;

import org.freda.thrones.consumer.ThronesService;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:35
 * @Package org.freda.thrones.consumer.blancer
 * @Describtion
 * Define the Balance method to select the service node to invoke
 * @file ThronesBlancer.java
 */
public interface ThronesBalancer {

    /**
     * get the service register on the context use the algo
     * @param name service name
     * @param algo balance algorithm
     * @return
     */
    public ThronesService getService(String name,BalancerAlgo algo);
}
