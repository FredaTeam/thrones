package org.freda.thrones.registry.consul.model;

import java.util.Arrays;

/**
 * Create on 2018/9/30 08:43
 */
public enum ConsulRoleType {

    CONSUMER(0),

    PROVIDER(2);

    private final int value;

    ConsulRoleType(int value) {
        this.value = value;
    }

    public static ConsulRoleType ofValue(int value) {
        return Arrays.stream(ConsulRoleType.values())
                .filter(it -> value == it.getValue())
                .findAny()
                .orElseThrow(() -> new RuntimeException("unknown value for ConsulRoleType"));
    }

    public int getValue() {
        return value;
    }
}
