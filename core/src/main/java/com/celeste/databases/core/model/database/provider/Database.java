package com.celeste.databases.core.model.database.provider;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.database.type.DatabaseType;

public interface Database {

  void init() throws FailedConnectionException;

  void shutdown() throws FailedShutdownException;

  boolean isClosed() throws FailedConnectionException;

  DatabaseType getDatabaseType();

}
