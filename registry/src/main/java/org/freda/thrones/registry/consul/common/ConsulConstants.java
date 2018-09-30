package org.freda.thrones.registry.consul.common;

import java.util.regex.Pattern;

/**
 * Create on 2018/9/30 09:52
 */
public class ConsulConstants {

    /**
     * service 最长存活周期（Time To Live），单位秒。 每个service会注册一个ttl类型的check，在最长TTL秒不发送心跳 就会将service变为不可用状态。
     */
    public static int TTL = 30;

    /**
     * 心跳周期，取ttl的2/3
     */
    public static int HEARTBEAT_CIRCLE = (TTL * 1000 * 2) / 3 / 10;

    /**
     * consul服务查询默认间隔时间。单位毫秒
     */
    public static int DEFAULT_LOOKUP_INTERVAL = 30000;

    /**
     * consul block 查询时 block的最长时间,单位，分钟
     */
    public static int CONSUL_BLOCK_TIME_MINUTES = 10;

    /**
     * consul block 查询时 block的最长时间,单位，秒
     */
    public static long CONSUL_BLOCK_TIME_SECONDS = CONSUL_BLOCK_TIME_MINUTES * 60;

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    public static final String DEFAULT_VERSION = "0.0.0";
    public static final String LOCALHOST_KEY = "localhost";
    public static final String ANYHOST_KEY = "anyhost";
    public static final String ANYHOST_VALUE = "0.0.0.0";

    public static final String CONSUL_SERVICE_PRE = "consul_";
    public static final String PATH_SEPARATOR = "/";
    public static final String PROVIDERS_CATEGORY = "providers";
    public static final String CONSUMERS_CATEGORY = "consumers";
}
