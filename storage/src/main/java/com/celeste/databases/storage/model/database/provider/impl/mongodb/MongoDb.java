package com.celeste.databases.storage.model.database.provider.impl.mongodb;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.databases.storage.model.database.dao.impl.mongodb.MongoDbDao;
import com.celeste.databases.storage.model.database.provider.Storage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoDb extends Storage {

  MongoClient getClient();

  MongoDatabase getDatabase();

  @Override
  default <T> StorageDao<T> createDao(final Class<T> entity) throws FailedConnectionException {
    return new MongoDbDao<>(this, entity);
  }

}
