package com.celeste;

import java.util.Properties;

public interface ConnectionProvider<T> {

    /**
     * @return T Connection instance from provider.
     */
    T getConnectionInstance();

    /**
     * @return boolean If provider is connected
     */
    boolean isRunning();

    /**
     * @param properties Properties with provider credentials.
     */
    void connect(Properties properties);

    /**
     * Disconnects provider.
     */
    void disconnect();

}
