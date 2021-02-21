package com.celeste.provider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public interface MongoDB {

    boolean isConnect();

    MongoClient getClient();

    MongoDatabase getDatabase();

}
