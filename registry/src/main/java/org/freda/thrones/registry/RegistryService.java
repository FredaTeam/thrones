package org.freda.thrones.registry;

import org.freda.thrones.framework.common.URL;

import java.util.List;

/**
 * Create on 2018/9/14 10:06
 */
public interface RegistryService {

    /**
     * register the url to the registry center (consul)
     */
    void register(URL url);

    /**
     * unregister the url from the registry
     */
    void unregister(URL url);

    /**
     * subscribe the providers,meanwhile define the listener when the service has changed
     */
    void subscribe(URL url, NotifyListener listener);

    /**
     * unsubscribe the url
     */
    void unsubscribe(URL url);

    /**
     * find URL list by the condition of given url
     * <p>
     * <p>
     * use for NotifyListener
     *
     * @see NotifyListener#notify(java.util.List)
     */
    List<URL> lookup(URL url);
}
