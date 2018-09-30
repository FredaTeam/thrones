package org.freda.thrones.framework.common;

import com.google.common.collect.Maps;
import lombok.Data;
import org.freda.thrones.framework.constants.Constants;
import org.freda.thrones.common.utils.NetUtils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * an detailed address for client to server communication
 * <p>
 * the whole path as follows:
 * protocol://secret@host:port/path?param1=value1&param2=value2
 */
@Data
public class URL implements Serializable {

    /**
     * such as provider consumer ...
     */
    private String protocol;

    /**
     * the secret of url which can be empty
     */
    private String secret;

    /**
     * hostname or ip address
     */
    private String host;

    /**
     * port
     */
    private int port;

    /**
     * service name
     */
    private String path;

    /**
     * additional params
     */
    private Map<String, String> params;

    // ==============cache==============
    private volatile transient Map<String, Number> numbers;

    private volatile transient String ip;

    private volatile transient String full;


    public URL(String protocol, String host, int port) {
        this(protocol, null, host, port, null, null);
    }

    public URL(String protocol, String host, int port, String path) {
        this(protocol, null, host, port, path, null);
    }

    public URL(String protocol, String secret, String host, int port) {
        this(protocol, secret, host, port, null, null);
    }

    public URL(String protocol, String secret, String host, int port, String path) {
        this(protocol, secret, host, port, path, null);
    }

    public URL(String protocol, String secret, String host, int port, String path, Map<String, String> params) {
        this.protocol = protocol;
        this.secret = secret;
        this.host = host;
        this.port = port < 0 ? 0 : port;
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        this.params = Collections.unmodifiableMap(params == null ? Maps.newHashMap() : params);
    }

    @Override
    public String toString() {
        return "URL{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String getIp() {
        if (ip == null) {
            ip = NetUtils.getIpByHost(host);
        }
        return ip;
    }

    /**
     * Parse url string
     *
     * @param url URL string
     * @return URL instance
     * @see URL
     */
    public static URL valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String secret = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = Maps.newHashMap();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }
        i = url.lastIndexOf("@");
        if (i >= 0) {
            secret = url.substring(0, i);
            url = url.substring(i + 1);
        }
        i = url.lastIndexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0) {
            host = url;
        }
        return new URL(protocol, secret, host, port, path, parameters);
    }

    public String toFullString() {
        if (full != null) {
            return full;
        }
        return full = buildString(true, true);
    }

    private String buildString(boolean appendSecret, boolean appendParameter, String... parameters) {
        return buildString(appendSecret, appendParameter, false, false, parameters);
    }

    private String buildString(boolean appendUser, boolean appendParameter, boolean useIP, boolean useService, String... parameters) {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        if (appendUser && secret != null && secret.length() > 0) {
            buf.append(secret);
            buf.append("@");
        }
        String host;
        if (useIP) {
            host = getIp();
        } else {
            host = getHost();
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        String path;
        if (useService) {
            path = getServiceKey();
        } else {
            path = getPath();
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }
        if (appendParameter) {
            buildParams(buf, true, parameters);
        }
        return buf.toString();
    }

    private void buildParams(StringBuilder buf, boolean concat, String[] parameters) {
        if (getParams() != null && getParams().size() > 0) {
            List<String> includes = (parameters == null || parameters.length == 0 ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(getParams()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0
                        && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }

    public String getServiceKey() {
        String inf = getServiceInterface();
        if (inf == null) return null;
        StringBuilder buf = new StringBuilder();
        String group = getParam(Constants.PARAMETER.GROUP_KEY);
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(inf);
        String version = getParam(Constants.PARAMETER.VERSION_KEY);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public String getServiceInterface() {
        return getParam(Constants.PARAMETER.INTERFACE_KEY, path);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public URL addParam(String key, String value) {
        if (key == null || key.length() == 0
                || value == null || value.length() == 0) {
            return this;
        }
        // if value doesn't change, return immediately
        if (value.equals(getParams().get(key))) { // value != null
            return this;
        }

        Map<String, String> map = Maps.newHashMap(getParams());
        map.put(key, value);
        return new URL(protocol, secret, host, port, path, map);
    }

    public URL addParam(Map<String, String> parameters) {
        if (parameters.size() == 0) {
            return this;
        }

        boolean hasAndEqual = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = getParams().get(entry.getKey());
            if (value == null) {
                if (entry.getValue() != null) {
                    hasAndEqual = false;
                    break;
                }
            } else {
                if (!value.equals(entry.getValue())) {
                    hasAndEqual = false;
                    break;
                }
            }
        }
        if (hasAndEqual) {
            return this;
        }
        Map<String, String> map = Maps.newHashMap(getParams());
        map.putAll(parameters);
        return new URL(protocol, secret, host, port, path, map);
    }

    public int getPositiveParam(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParam(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getParam(String key, int defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.intValue();
        }
        String value = getParam(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        getNumbers().put(key, i);
        return i;
    }

    public String getParam(String key, String defaultValue) {
        String value = getParam(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }


    public boolean getParam(String key, boolean defaultValue) {
        String value = getParam(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public String getParam(String key) {
        String value = params.get(key);
        if (value == null || value.length() == 0) {
            value = params.get(Constants.PARAMETER.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    private Map<String, Number> getNumbers() {
        if (numbers == null) { // concurrent initialization is tolerant
            numbers = new ConcurrentHashMap<String, Number>();
        }
        return numbers;
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public URL setAddress(String address) {
        int i = address.lastIndexOf(':');
        String host;
        int port = this.port;
        if (i >= 0) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
        }
        return new URL(protocol, secret, host, port, path, getParams());
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }
}
