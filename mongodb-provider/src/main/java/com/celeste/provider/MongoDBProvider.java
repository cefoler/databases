package com.celeste.provider;

import com.celeste.ConnectionProvider;
import com.mongodb.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Getter(onMethod_= { @Synchronized } )
public class MongoDBProvider implements ConnectionProvider<MongoClient> {

    private MongoClient client;
    private Morphia morphia;
    private Datastore datastore;

    @Override @Synchronized @SneakyThrows
    public void connect(Properties properties) {
        Class.forName("com.mongodb.MongoClient");

        final ServerAddress address = new ServerAddress(
            properties.getProperty("hostname"),
            Integer.parseInt(properties.getProperty("port"))
        );

        final List<MongoCredential> credentials = Arrays.asList(
            MongoCredential.createCredential(
                properties.getProperty("username"),
                properties.getProperty("database"),
                properties.getProperty("password").toCharArray()
            )
        );

        final MongoClientOptions options = MongoClientOptions.builder()
            .minConnectionsPerHost(1)
            .connectionsPerHost(20)
            .connectTimeout(30000)
            .socketTimeout(30000)
            .maxConnectionIdleTime(600000)
            .maxConnectionLifeTime(1800000)
            .retryWrites(true)
            .readPreference(ReadPreference.primaryPreferred())
            .build();

        this.morphia = new Morphia();
        this.client = new MongoClient(address, credentials, options);
        this.datastore = morphia.createDatastore(client, properties.getProperty("database"));

        getDatastore().ensureIndexes();
    }

    @Override
    public MongoClient getConnectionInstance() {
        return client;
    }

    @Override
    public boolean isRunning() {
        return client != null;
    }

    @Override @Synchronized
    public void disconnect() {
        client.close();
    }

}