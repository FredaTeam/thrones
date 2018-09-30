package org.freda.thrones.registry.consul.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Create on 2018/9/30 10:01
 */
@Data
@AllArgsConstructor
public class HeartBeatService {

    private ConsulService consulService;

    private ConsulSession consulSession;
}
