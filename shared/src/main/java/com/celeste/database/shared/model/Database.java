package com.celeste.database.shared.model;

import com.celeste.database.shared.exception.database.FailedConnectionException;
import com.celeste.database.shared.model.type.DatabaseType;
import org.jetbrains.annotations.NotNull;

public interface Database {

  void init() throws FailedConnectionException;

  void shutdown();

  boolean isClosed();

  @NotNull
  DatabaseType getDatabaseType();

}