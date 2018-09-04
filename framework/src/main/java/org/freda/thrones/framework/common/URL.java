package org.freda.thrones.framework.common;

import com.google.common.collect.Maps;
import lombok.Data;
import org.freda.thrones.framework.constants.Constants;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
     * current time use self protocal: "thrones"
     */
    private String protocal;

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


    public URL(String protocal, String host, int port) {
        this(protocal, null, host, port, null, null);
    }

    public URL(String protocal, String host, int port, String path) {
        this(protocal, null, host, port, path, null);
    }

    public URL(String protocal, String secret, String host, int port) {
        this(protocal, secret, host, port, null, null);
    }

    public URL(String protocal, String secret, String host, int port, String path) {
        this(protocal, secret, host, port, path, null);
    }

    public URL(String protocal, String secret, String host, int port, String path, Map<String, String> params) {
        this.protocal = protocal;
        this.secret = secret;
        this.host = host;
        this.port = port < 0 ? 0 : port;
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        this.params = Collections.unmodifiableMap(params == null ? Maps.newHashMap() : params);
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


}
