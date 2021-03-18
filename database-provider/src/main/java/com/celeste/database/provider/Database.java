package com.celeste.database.provider;

import com.celeste.database.type.DatabaseType;
import com.celeste.exception.DatabaseException;
import org.jetbrains.annotations.NotNull;

public interface Database {

    void init() throws DatabaseException;

    void shutdown();

    boolean isConnect();

    @NotNull
    DatabaseType getType();

}