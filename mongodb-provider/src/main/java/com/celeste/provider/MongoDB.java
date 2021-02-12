package com.celeste.provider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public interface MongoDB {

    void createCollection(final String name);

    boolean isConnect();

    MongoClient getClient();

    MongoDatabase getDatabase();

    MongoCollection<Document> getCollection(final String name);

    <T> MongoCollection<T> getCollection(final String name, final Class<T> clazz);

}
