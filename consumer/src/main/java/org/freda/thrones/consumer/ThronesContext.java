package org.freda.thrones.consumer;

import org.freda.thrones.consumer.blancer.ThronesBalancer;

import java.util.List;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:28
 * @Package org.freda.thrones.consumer
 * @Describtion
 *
 *  service will be saved in the Map<String,Tree> and
 * @see org.freda.thrones.consumer.blancer.ThronesServiceTree
 *  should choice a algorithm to select the service entity
 *
 * @file ThronesContext.java
 */
public interface ThronesContext {

    /**
     * use the service name to get the service entity
     * @param name service name
     * @return
     */
    public List<ThronesService> getService(String name);

    /**
     * get the balancer by default
     * @return
     */
    public ThronesBalancer getBalancer();

    /**
     * get the serial number when call a service
     * @return
     */
    public long getSenquence();

    /**
     * refresh the service list time will be default 10000ms
     * @return
     */
    public long getRefreshTime();

}
