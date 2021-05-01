package com.celeste.databases.core.model.database.provider;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.DatabaseType;
import org.jetbrains.annotations.NotNull;

public interface Database {

  void init() throws FailedConnectionException;

  void shutdown();

  boolean isClosed();

  @NotNull
  DatabaseType getDatabaseType();

}
