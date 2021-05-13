package com.celeste.databases.storage.model.database.provider;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.type.DatabaseType;
import com.celeste.databases.storage.model.database.type.StorageType;

public interface Storage extends Database {

  StorageType getStorageType();

  @Override
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}
