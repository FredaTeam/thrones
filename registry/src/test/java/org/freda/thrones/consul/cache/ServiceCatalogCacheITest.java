package org.freda.thrones.consul.cache;

import com.google.common.util.concurrent.Uninterruptibles;
import com.orbitz.consul.cache.ServiceCatalogCache;
import com.orbitz.consul.model.catalog.CatalogService;
import org.freda.thrones.consul.BaseIntegrationTest;
import org.freda.thrones.consul.Synchroniser;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceCatalogCacheITest extends BaseIntegrationTest {

    private static final List<String> NO_TAGS = Collections.emptyList();
    private static final Map<String, String> NO_META = Collections.emptyMap();

    @Test
    public void testWatchService() throws InterruptedException {
        String name = UUID.randomUUID().toString();
        String serviceId1 = createAutoDeregisterServiceId();
        String serviceId2 = createAutoDeregisterServiceId();

        List<Map<String, CatalogService>> result = new CopyOnWriteArrayList<>();
        CountDownLatch finish = new CountDownLatch(3);

        ServiceCatalogCache cache = ServiceCatalogCache.newCache(client.catalogClient(), name);
        cache.addListener(serviceMap -> {
            result.add(serviceMap);
            finish.countDown();
        });

        cache.start();
        cache.awaitInitialized(3, TimeUnit.SECONDS);

        client.agentClient().register(20001, 20, name, serviceId1, NO_TAGS, NO_META);
        Synchroniser.pause(Duration.ofMillis(100));
        client.agentClient().register(20002, 20, name, serviceId2, NO_TAGS, NO_META);

        Uninterruptibles.awaitUninterruptibly(finish, 1, TimeUnit.SECONDS);

        assertEquals(0, result.get(0).size());
        assertEquals(1, result.get(1).size());
        assertEquals(2, result.get(2).size());

        assertTrue(result.get(1).containsKey(serviceId1));
        assertFalse(result.get(1).containsKey(serviceId2));

        assertTrue(result.get(2).containsKey(serviceId1));
        assertTrue(result.get(2).containsKey(serviceId2));

        assertEquals(serviceId1, result.get(1).get(serviceId1).getServiceId());
        assertEquals(serviceId2, result.get(2).get(serviceId2).getServiceId());
    }
}
