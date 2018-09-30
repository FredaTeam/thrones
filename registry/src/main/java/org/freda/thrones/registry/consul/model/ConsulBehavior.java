package org.freda.thrones.registry.consul.model;

/**
 * Create on 2018/9/30 09:02
 */
public enum ConsulBehavior {

    RELEASE("release"),

    DELETE("delete");

    private final String value;

    ConsulBehavior(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
