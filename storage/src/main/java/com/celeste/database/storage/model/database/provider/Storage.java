package com.celeste.database.storage.model.database.provider;

import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.Database;
import com.celeste.database.shared.model.database.type.DatabaseType;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.database.type.StorageType;
import com.celeste.database.storage.model.Storable;
import org.jetbrains.annotations.NotNull;

public interface Storage extends Database {

  @NotNull
  StorageType getStorageType();

  @NotNull
  <T extends Storable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity) throws DAOException;

  @Override @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.STORAGE;
  }

}