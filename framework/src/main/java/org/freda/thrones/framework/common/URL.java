package org.freda.thrones.framework.common;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

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
}
