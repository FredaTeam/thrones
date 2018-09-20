package org.freda.thrones.registry;

import org.freda.thrones.framework.common.URL;

/**
 * Create on 2018/9/14 09:30
 */
public interface Registry extends RegistryService {

    /**
     * get the url of the registry
     *
     * @return URL
     */
    URL getUrl();

    /**
     * check the registry is avaliable or not
     *
     * @return boolean
     */
    boolean isAvailable();

    /**
     * destroy the registry
     */
    void destroy();
}
