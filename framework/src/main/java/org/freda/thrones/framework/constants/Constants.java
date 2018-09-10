package org.freda.thrones.framework.constants;

public class Constants {

    // ms
    public static final int DEFAULT_TIMEOUT = 1000;

    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
}
