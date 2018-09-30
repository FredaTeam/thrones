package org.freda.thrones.registry.consul.support;

import com.google.common.collect.Sets;
import com.orbitz.consul.AgentClient;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.remote.ThronesThreadFactory;
import org.freda.thrones.registry.consul.common.ConsulConstants;
import org.freda.thrones.registry.consul.model.ConsulService;
import org.freda.thrones.registry.consul.model.ConsulSession;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * heartbeat to registry including consul service & consul session
 * Create on 2018/9/29 16:25
 */
@Slf4j
public class TtlScheduler {

    // all consul service
    private final Set<ConsulService> consulServices = Sets.newConcurrentHashSet();

    // check fail consul service
    private final Set<ConsulService> failConsulServices = Sets.newConcurrentHashSet();

    private final Set<ConsulSession> consulSessions = Sets.newConcurrentHashSet();

    private final Set<ConsulSession> failConsulSessions = Sets.newConcurrentHashSet();

    private final AgentClient agent;

    static {
        ScheduledExecutorService consulServiceExecutor = Executors.newScheduledThreadPool(1,
                new ThronesThreadFactory("ConsulServiceTtl", true));

        ScheduledExecutorService consulSessionExecutor = Executors.newScheduledThreadPool(1,
                new ThronesThreadFactory("ConsulSessionTtl", true));
        consulServiceExecutor.scheduleAtFixedRate(new ConsulServiceHeartBeatTask(),
                ConsulConstants.HEARTBEAT_CIRCLE,
                ConsulConstants.HEARTBEAT_CIRCLE,
                TimeUnit.MILLISECONDS);
        consulSessionExecutor.scheduleAtFixedRate(new ConsulSessionHeartBeatTask(),
                ConsulConstants.HEARTBEAT_CIRCLE,
                ConsulConstants.HEARTBEAT_CIRCLE,
                TimeUnit.MILLISECONDS);
    }

    public TtlScheduler(AgentClient agent) {
        this.agent = agent;

    }

    public void addConsulService(ConsulService consulService) {
        consulServices.add(consulService);
    }

    public void removeConsulService(ConsulService consulService) {
        consulServices.remove(consulService);
    }

    public void addConsulSession(ConsulSession consulSession) {
        consulSessions.add(consulSession);
    }

    public Set<ConsulService> getFailConsulServices() {
        return failConsulServices;
    }

    public Set<ConsulSession> getFailConsulSessions() {
        return failConsulSessions;
    }

    public void cleanFailTtl() {
        failConsulServices.clear();
        failConsulSessions.clear();
    }

    private static class ConsulServiceHeartBeatTask implements Runnable {

        @Override
        public void run() {

        }
    }

    private static class ConsulSessionHeartBeatTask implements Runnable {

        @Override
        public void run() {

        }
    }
}
