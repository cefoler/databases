package com.celeste.databases.storage.model.database.provider;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.DatabaseType;
import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.databases.storage.model.database.type.StorageType;

public interface Storage extends Database {

  StorageType getStorageType();

  <T> StorageDao<T> createDao(final Class<T> entity) throws FailedConnectionException;

  @Override
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}
