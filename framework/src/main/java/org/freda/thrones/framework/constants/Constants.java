package org.freda.thrones.framework.constants;

public interface Constants {


    interface PARAMETER {
        String PORT_KEY = "port";

        String VERSION_KEY = "version";

        String GROUP_KEY = "group";

        String TIME_OUT = "timeout";

        String DEFAULT_KEY_PREFIX = "default.";

        String INTERFACE_KEY = "interface";

        String TOKEN_KEY = "token";

        String TIMEOUT_KEY = "timeout";

    }

    interface VALUE {

        // unit: ms
        int DEFAULT_TIMEOUT = 1000;

        int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    }
}
