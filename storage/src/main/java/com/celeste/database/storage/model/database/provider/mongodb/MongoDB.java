package com.celeste.database.storage.model.database.provider.mongodb;

import com.celeste.database.shared.exception.dao.DAOException;
import com.celeste.database.shared.exception.database.FailedConnectionException;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.dao.mongodb.MongoDBDAO;
import com.celeste.database.storage.model.database.provider.Storage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jetbrains.annotations.NotNull;

public interface MongoDB extends Storage {

  /**
   * Returns the MongoClient
   *
   * @return MongoClient
   * @throws FailedConnectionException Throws if the connection between the database failed
   */
  @NotNull
  MongoClient getClient() throws FailedConnectionException;

  /**
   * Returns the MongoDatabase
   *
   * @return MongoDatabase
   * @throws FailedConnectionException Throws if the connection between the database failed
   */
  @NotNull
  MongoDatabase getDatabase() throws FailedConnectionException;

  /**
   * Creates a new StorageDAO for MongoDB
   *
   * @param entity Object
   * @param <T>    T
   * @return StorageDAO
   * @throws DAOException Throws if the it wasn't possible to create the DAO
   */
  @Override
  @NotNull
  default <T extends Storable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity)
      throws DAOException {
    return new MongoDBDAO<>(this, entity);
  }

}