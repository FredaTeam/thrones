package org.freda.thrones.registry;

import org.freda.thrones.framework.common.URL;

import java.util.List;

/**
 * Create on 2018/9/14 09:56
 */
public interface NotifyListener {

    /**
     * when a service changed, this listener will notify its subscribers
     * <p>
     * the urls is searched as follow method
     *
     * @see Registry#lookup(org.freda.thrones.framework.common.URL)
     */
    void notify(List<URL> urls);
}
