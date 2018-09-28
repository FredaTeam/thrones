package org.freda.thrones.registry.consul;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.registry.NotifyListener;
import org.freda.thrones.registry.support.AbstractRegistry;

import java.util.List;

/**
 * Create on 2018/9/14 09:31
 */
public class ConsulRegistry extends AbstractRegistry {

    @Override
    public void register(URL url) {

    }

    @Override
    public void unregister(URL url) {

    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {

    }

    @Override
    public void unsubscribe(URL url) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() {

    }
}
