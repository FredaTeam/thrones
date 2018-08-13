package org.freda.thrones.consumer.blancer;

import org.freda.thrones.consumer.ThronesService;
import org.freda.thrones.consumer.blancer.BalancerAlgo;

import java.util.List;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午5:14
 * @Package org.freda.thrones.consumer
 * @Describtion
 *
 * use the Tree base structure to organize the service entity
 *
 * @see BalancerAlgo
 *
 * algorithm will decide the travies in this tree method ,and select the right service node right
 *
 * and the all service will be as a node in the structure
 * when the weight of a service node change then will rorate the node with the others node .
 *
 * and the node attrs are below and should need some methods to find the node use these attributes
 *
 * endpoint  the service deploy point
 * weight
 * service name
 * link number
 * live time
 * stab rate (use the successful to count )
 *
 * @file ThronesServiceList.java
 */
public interface ThronesServiceTree {

    /**
     * add a service entity node into this tree
     * @param service
     */
    public void addService(ThronesService service);

    /**
     * remove the node
     * @param service
     */
    public void removeService(ThronesService service);

    /**
     * use algorithm to select the node
     * @param algo
     * @return
     */
    public ThronesService getService(BalancerAlgo algo);

    /**
     * select the root node to use
     * @return
     */
    public ThronesService peekService();

}
