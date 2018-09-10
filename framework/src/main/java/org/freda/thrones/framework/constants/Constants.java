package org.freda.thrones.framework.constants;

public interface Constants {


    int DEFAULT_CONNECT_TIMEOUT = 3000;

    interface PARAMETER {
        String PORT_KEY = "port";

        String VERSION_KEY = "version";

        String GROUP_KEY = "group";

        String TIME_OUT = "timeout";

        String DEFAULT_KEY_PREFIX = "default.";

        String INTERFACE_KEY = "interface";

        String TOKEN_KEY = "token";

        String TIMEOUT_KEY = "timeout";

        String CONNECT_TIMEOUT_KEY = "connect.timeout";

        String IS_SERVER_KEY = "isserver";

        String RECONNECT_KEY = "reconnect";
    }

    interface VALUE {

        // unit: ms
        int DEFAULT_TIMEOUT = 1000;

        int DEFAULT_CONNECT_TIMEOUT = 3000;

        int DEFAULT_RECONNECT_PERIOD = 2000;

        int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    }
}
