package com.celeste.provider;

import java.util.Properties;

public interface ConnectionProvider<T> {

    T getConnectionInstance();

    boolean isRunning();

    boolean connect(Properties properties);

    void disconnect();

}
