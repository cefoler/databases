package com.celeste.database.shared.model.database.provider;

import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.shared.model.database.type.DatabaseType;
import org.jetbrains.annotations.NotNull;

public interface Database {

  void init() throws FailedConnectionException;

  void shutdown();

  boolean isClose();

  @NotNull
  DatabaseType getDatabaseType();

}