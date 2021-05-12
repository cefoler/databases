package com.celeste.databases.storage.model.database.provider;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.type.DatabaseType;

public interface Storage extends Database {

  @Override
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}
