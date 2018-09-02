package org.freda.thrones.framework.constants;

public interface Constants {


    interface PARAMETER {
        String TIME_OUT = "timeout";

        String DEFAULT_KEY_PREFIX = "default.";

    }

    interface VALUE {

        // unit: ms
        int DEFAULT_TIMEOUT = 1000;

        int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);
    }
}
