package org.freda.thrones.framework.registry;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.cache.KVCache;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ImmutableServiceHealth;
import com.orbitz.consul.model.health.ServiceHealth;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create on 2018/9/10 21:54
 */
public class ConsulTest {

    private static final Lock LOCK = new ReentrantLock();
    private static final Condition STOP = LOCK.newCondition();


    public static void main(String[] args) throws NotRegisteredException, InterruptedException {

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress("localhost", 8080));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

//        Consul client = Consul.builder().build(); // connect on localhost
//
//        AgentClient agentClient = client.agentClient();
//
//        String serviceId = "1";
//        Registration service = ImmutableRegistration.builder()
//                .id(serviceId)
//                .name("myService")
//                .port(8080)
//                .check(Registration.RegCheck.ttl(3L)) // registers with a TTL of 3 seconds
//                .tags(Collections.singletonList("tag1"))
//                .meta(Collections.singletonMap("version", "1.0"))
//                .build();
//
//        agentClient.register(service);
//
//        final KeyValueClient kvClient = agentClient.keyValueClient();
//        KVCache kvCache = KVCache.newCache(kvClient, "foo");

        // Check in with ConsulTest (serviceId required only).
        // Client will prepend "service:" for service level checks.
        // Note that you need to continually check in before the TTL expires, otherwise your service's state will be marked as "critical".
//        agentClient.pass(serviceId);


        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("localhost:" + 8500)).build();
//        AgentClient agent = consul.agentClient();
//
//        ImmutableRegCheck check = ImmutableRegCheck.builder().tcp("localhost:" + 8080).interval("3s").build();
//        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
//        builder.id("mikeservice_id")
//                .name("mikeservice")
//                .addTags("v1")
//                .address("localhost")
//                .port(8080)
//                .addChecks(check);
//
//        agent.register(builder.build());


        HealthClient client = consul.healthClient();
        String name = "mikeservice";
        ConsulResponse<List<ServiceHealth>> object = client.getAllServiceInstances(name);

        List<ServiceHealth> serviceHealths = object.getResponse();
        serviceHealths.forEach(it -> System.out.println(it.getService().getId()+"  "+it.getService().getAddress() + ":" + it.getService().getPort()));

//        addHook();

        LOCK.lock();
        STOP.await();
    }

    private static void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            try {
                LOCK.lock();
                STOP.signal();
            } finally {
                LOCK.unlock();
            }
        }, "StartMain-shutdown-hook"));
    }
}
