package org.freda.thrones.consumer.blancer;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:53
 * @Package org.freda.thrones.consumer.blancer
 * @Describtion
 * TODO  complete the policy type
 * will be used in the configuration file or the annotation
 * parser result will be this class type ;
 * @file BalancerPolicy.java
 */
public enum BalancerPolicy {
    ;

    BalancerPolicy(BalancerAlgo algo, String name, int weight) {
        this.algo = algo;
        this.name = name;
        this.weight = weight;
    }

    private BalancerAlgo algo;

    private String name;

    private int weight;

    public BalancerAlgo getAlgo(){
        return algo;
    }

    public String getName(){
        return name;
    }

    public int getWeight(){
        return weight;
    }
}
