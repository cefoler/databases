package com.celeste.redis;

import java.util.Properties;

public interface Connection<T> {

    /**
     * Returns the Connection instance
     *
     * @return T
     */
    T getConnectionInstance();

    /**
     * Check if the instance is running
     *
     * @return boolean
     */
    boolean isRunning();

    /**
     * Connects the provider with the following credentials
     *
     * @param properties Properties
     * @param credentials boolean if true, it uses the password
     */
    void connect(final Properties properties, final boolean credentials);

    void disconnect();

}
