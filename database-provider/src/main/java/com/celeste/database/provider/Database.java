package com.celeste.database.provider;

import com.celeste.database.type.DatabaseType;
import com.celeste.exception.FailedConnectionException;
import org.jetbrains.annotations.NotNull;

public interface Database {

    void init() throws FailedConnectionException;

    void shutdown();

    boolean isConnect();

    @NotNull
    DatabaseType getType();
}