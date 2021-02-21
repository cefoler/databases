package com.celeste.provider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import lombok.Getter;
import lombok.Synchronized;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.logging.Logger;

import static com.mongodb.MongoClientSettings.builder;
import static java.util.logging.Level.WARNING;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Getter(onMethod_= { @Synchronized } )
public class MongoProvider implements MongoDB {

    private static final String CONNECTION_URL = "mongodb://<username>:<password>@<host>/?authSource=admin";

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
    public boolean isConnect() {
        return client != null;
    }

    @Override
    public MongoClient getClient() {
        return client;
    }

}