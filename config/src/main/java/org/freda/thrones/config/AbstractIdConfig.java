package org.freda.thrones.config;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create on 2018/9/28 17:58
 */
public abstract class AbstractIdConfig implements Serializable {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private String id;

    public String getId() {
        return "thrones-rpc-cfg" + ID_GENERATOR.getAndIncrement();
    }
}
