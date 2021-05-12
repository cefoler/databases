package com.celeste.databases.storage.model.database.provider.impl.mongodb;

import com.celeste.databases.storage.model.database.provider.Storage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoDb extends Storage {

  MongoClient getClient();

  MongoDatabase getDatabase();

}
