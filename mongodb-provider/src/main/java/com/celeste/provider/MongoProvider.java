package com.celeste.provider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import lombok.Getter;
import lombok.Synchronized;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.logging.Logger;

import static com.mongodb.MongoClientSettings.builder;
import static java.util.logging.Level.WARNING;
import static lombok.AccessLevel.NONE;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Getter(onMethod_= { @Synchronized } )
public class MongoProvider implements MongoDB {

    private static final String CONNECTION_URL = "mongodb://<username>:<password>@<host>/?authSource=admin";
    @Getter(NONE)
    private MongoClient client;
    private MongoDatabase database;

    @Synchronized
    public void init(
        String username, String password, String host, String database
    ) {
        try {
            Class.forName("org.mongodb.driver");
            Logger.getLogger("org.mongodb.driver").setLevel(WARNING);

            final ConnectionString connection = new ConnectionString(
                CONNECTION_URL
                .replace("<username>", username)
                .replace("<password>", password)
                .replace("<host>", host)
            );

            final CodecRegistry codec = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder()
                        .automatic(true)
                        .build())
            );

            final MongoClientSettings settings = builder()
                    .applyConnectionString(connection)
                    .codecRegistry(codec)
                    .retryWrites(true)
                    .build();

            this.client = MongoClients.create(settings);
            this.database = client.getDatabase(database);
        } catch (ClassNotFoundException exception) {
            System.err.printf("Not possible connect to Mongo: %s", exception.getMessage());
        }
    }

    @Synchronized
    public void shutdown() {
        client.close();
    }

    @Override @Synchronized
    public void createCollection(final String name) {
        if (!exists(name)) database.createCollection(name);
    }

    @Override @Synchronized
    public boolean isConnect() {
        return client != null;
    }

    @Override
    public MongoClient getClient() {
        return null;
    }

    @Override @Synchronized
    public MongoCollection<Document> getCollection(final String name) {
        if (!exists(name)) database.createCollection(name);
        return database.getCollection(name);
    }

    @Override @Synchronized
    public <T> MongoCollection<T> getCollection(final String name, final Class<T> clazz) {
        if (!exists(name)) database.createCollection(name);
        return database.getCollection(name, clazz);
    }

    @Synchronized
    private boolean exists(final String name) {
        final MongoIterable<String> collections = database.listCollectionNames();

        for (final String collection : collections) {
            if (collection.equals(name)) return true;
        }

        return false;
    }

}