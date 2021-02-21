package com.celeste;

import com.celeste.provider.MongoProvider;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;

public class MongoDBProcessor {

    private Morphia morphia;
    private MongoProvider provider;
    private MongoClient client;

    @Getter
    public Datastore datastore;

    public void start(
        String username, String password, String host, String database, String mapPackage
    ) {
        this.morphia = new Morphia();
        this.client = new MongoClient();
        this.provider = new MongoProvider();

        morphia.mapPackage(mapPackage);
        this.datastore = createDatastore(database);

        provider.init(username, password, host, database);
    }

    public Datastore createDatastore(String name) {
        final Datastore datastore = morphia.createDatastore(client, name);
        datastore.ensureIndexes();

        return datastore;
    }

}
