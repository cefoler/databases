package com.celeste.database.storage.model.database.provider.mongodb;

import com.celeste.database.shared.model.type.ConnectionType;
import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.dao.mongodb.MongoDBDAO;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jetbrains.annotations.NotNull;

public interface MongoDB extends Storage {

  @NotNull
  MongoClient getClient() throws FailedConnectionException;

  @NotNull
  ConnectionType getConnectionType();

  @NotNull
  MongoDatabase getDatabase() throws FailedConnectionException;

  @Override @NotNull
  default <T extends Storable> StorageDAO<T> createDAO(@NotNull final Class<T> entity) throws DAOException {
    return new MongoDBDAO<>(this, entity);
  }

}