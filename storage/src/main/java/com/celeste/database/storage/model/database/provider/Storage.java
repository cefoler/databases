package com.celeste.database.storage.model.database.provider;

import com.celeste.database.shared.exception.dao.DAOException;
import com.celeste.database.shared.model.Database;
import com.celeste.database.shared.model.type.DatabaseType;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.database.StorageType;
import org.jetbrains.annotations.NotNull;

public interface Storage extends Database {

  @NotNull
  StorageType getStorageType();

  @NotNull
  <T extends Storable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity)
      throws DAOException;

  @Override
  @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}