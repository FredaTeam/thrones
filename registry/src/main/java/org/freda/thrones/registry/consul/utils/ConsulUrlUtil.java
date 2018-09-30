package org.freda.thrones.registry.consul.utils;

import org.freda.thrones.common.utils.UrlUtils;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.registry.consul.common.ConsulConstants;
import org.freda.thrones.registry.consul.model.ConsulRoleType;

/**
 * Create on 2018/9/30 09:50
 */
public class ConsulUrlUtil {

    public static String ephemralNodePath(URL url, ConsulRoleType roleType) {
        return ConsulConstants.CONSUL_SERVICE_PRE + toCategoryPathIncludeVersion(url, roleType)
                + ConsulConstants.PATH_SEPARATOR + url.getAddress();
    }

    private static String toCategoryPathIncludeVersion(URL url, ConsulRoleType roleType) {
        switch (roleType) {
            case CONSUMER:
                return toServicePath(url) + ConsulConstants.PATH_SEPARATOR +
                        url.getParam(Constants.PARAMETER.VERSION_KEY, ConsulConstants.DEFAULT_VERSION) +
                        ConsulConstants.PATH_SEPARATOR +
                        ConsulConstants.CONSUMERS_CATEGORY;
            case PROVIDER:
                return toServicePath(url) + ConsulConstants.PATH_SEPARATOR +
                        url.getParam(Constants.PARAMETER.VERSION_KEY, ConsulConstants.DEFAULT_VERSION) +
                        ConsulConstants.PATH_SEPARATOR +
                        ConsulConstants.PROVIDERS_CATEGORY;
            default:
                throw new IllegalArgumentException("there is no role type");
        }
    }

    private static String toServicePath(URL url) {
        String name = url.getServiceInterface();
        String group = url.getParam(Constants.PARAMETER.GROUP_KEY);
        return group + ConsulConstants.PATH_SEPARATOR + UrlUtils.encode(name);
    }
}
