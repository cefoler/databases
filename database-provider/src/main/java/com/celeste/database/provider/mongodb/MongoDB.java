package com.celeste.database.provider.mongodb;

import com.celeste.database.provider.Database;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;

public interface MongoDB extends Database {

    MongoClient getClient();

    Datastore getDatastore();

}
