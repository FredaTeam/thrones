package org.freda.thrones.registry.consul.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Create on 2018/9/30 09:05
 */
@Data
@AllArgsConstructor
public class ConsulSession {

    private String sessionId;

    private ConsulEphemeralNode ephemeralNode;
}
