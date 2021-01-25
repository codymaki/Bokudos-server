package com.bokudos.bokudosserver.constants;

public class ServerConfigurationConstants {

    public final static int DEFAULT_SERVER_TICK_RATE = 60;
    public final static int MIN_SERVER_TICK_RATE = 15;

    /**
     * If we notice server slowdown, this is the default interval that the tick rate will be altered until the server is back at peak performance.
     */
    public final static int SERVER_TICK_RATE_INTERVAL = 2;
}
