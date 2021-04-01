package com.celeste.database.storage.model.database.provider.mongodb;

import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.dao.mongodb.MongoDBDAO;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import org.jetbrains.annotations.NotNull;

public interface MongoDB extends Storage {

  @NotNull
  MongoClient getClient() throws FailedConnectionException;

  @NotNull
  Datastore getDatastore() throws FailedConnectionException;

  @Override @NotNull
  default <T extends Storable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity) throws DAOException {
    return new MongoDBDAO<>(this, entity);
  }

}