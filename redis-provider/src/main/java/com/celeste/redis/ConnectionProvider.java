package com.celeste.redis;

import java.util.Properties;

public interface ConnectionProvider<T> {

    T getConnectionInstance();

    boolean isRunning();

    boolean connect(Properties properties, boolean credentials);

    void disconnect();

}
