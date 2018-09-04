package org.freda.thrones.framework.manager.support;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.framework.manager.Kernel;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.invoke.Invoker;

import java.util.Map;
import java.util.Set;

/**
 * Create on 2018/9/3 15:57
 */
@Slf4j
public abstract class AbstractKernel implements Kernel {

    /**
     * store exporter to control
     */
    protected final Map<String, Exporter<?>> exporterMap = Maps.newConcurrentMap();

    /**
     * invoker storation to control
     */
    protected final Set<Invoker<?>> invokers = Sets.newConcurrentHashSet();

    /**
     * the unique key for exporterMap
     * there is no host/ip because the service is apply on each server station
     */
    protected static String serviceKey(URL url) {
        int port = url.getParam(Constants.PARAMETER.PORT_KEY, url.getPort());
        return serviceKey(port,
                url.getPath(),
                url.getParam(Constants.PARAMETER.VERSION_KEY),
                url.getParam(Constants.PARAMETER.GROUP_KEY)
        );
    }

    protected static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        StringBuilder buf = new StringBuilder();
        if (serviceGroup != null && serviceGroup.length() > 0) {
            buf.append(serviceGroup);
            buf.append("/");
        }
        buf.append(serviceName);
        if (serviceVersion != null && serviceVersion.length() > 0 && !"0.0.0".equals(serviceVersion)) {
            buf.append(":");
            buf.append(serviceVersion);
        }
        buf.append(":");
        buf.append(port);
        return buf.toString();
    }
}
