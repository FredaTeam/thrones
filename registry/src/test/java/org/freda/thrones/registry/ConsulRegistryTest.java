package org.freda.thrones.registry;


import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.health.ServiceHealth;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create on 2018/9/10 21:54
 */
public class ConsulRegistryTest {

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


    }

    @Test
    public void test_register_service() throws InterruptedException {
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("localhost:" + 8500)).build();
        AgentClient agent = consul.agentClient();

        ImmutableRegCheck check = ImmutableRegCheck.builder().tcp("localhost:" + 8080).interval("3s").build();
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();

        String serviceName = UUID.randomUUID().toString();
        String serviceId = UUID.randomUUID().toString();

        builder.id(serviceId)
                .name(serviceName)
                .addTags("v1")
                .address("localhost")
                .port(8080)
                .addChecks(check);

        agent.register(builder.build());

    }


    @Test
    public void test_unregister_service(){
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("localhost:" + 8500)).build();
        AgentClient agent = consul.agentClient();

//        ImmutableRegCheck check = ImmutableRegCheck.builder().tcp("localhost:" + 8080).interval("3s").build();
//        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
//
//        String serviceName = "753d7292-6c85-4eb8-83a1-dc8ff1bae6b0";
//        HealthClient client = consul.healthClient();
//        ConsulResponse<List<ServiceHealth>> object = client.getHealthyServiceInstances(serviceName);


        agent.deregister("9ef7f3a0-0870-433b-8128-fe4dd6f5539d");

    }



    @Test
    public void test_getServiceInstance() throws InterruptedException {
        Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("localhost:" + 8500)).build();
        HealthClient client = consul.healthClient();
        String name = "myService";
        ConsulResponse<List<ServiceHealth>> object = client.getAllServiceInstances(name);

        List<ServiceHealth> serviceHealths = object.getResponse();
        serviceHealths.forEach(it -> System.out.println("serviceId: "+it.getService().getId()+"  check: "+it.getChecks().size() + "  address: " + it.getService().getAddress() + ":" + it.getService().getPort()));
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
