package com.celeste.storage.model.database.provider;

import com.celeste.shared.model.dao.exception.DAOException;
import com.celeste.shared.model.database.provider.Database;
import com.celeste.shared.model.database.type.DatabaseType;
import com.celeste.storage.model.dao.StorageDAO;
import com.celeste.storage.model.database.type.StorageType;
import com.celeste.storage.model.Serializable;
import org.jetbrains.annotations.NotNull;

public interface Storage extends Database {

  @NotNull
  StorageType getStorageType();

  @NotNull
  <T extends Serializable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity) throws DAOException;

  @Override @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}