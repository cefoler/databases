package com.celeste.database.provider.mongodb;

import com.celeste.database.type.DatabaseType;
import com.celeste.exception.DatabaseException;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Getter
public class MongoDBProvider implements MongoDB {

    @Getter(AccessLevel.PRIVATE)
    private final Properties properties;

    private MongoClient client;
    private Datastore datastore;

    public MongoDBProvider(@NotNull final Properties properties) throws DatabaseException {
        this.properties = properties;

        init();
    }

    @Override @Synchronized
    public void init() throws DatabaseException {
        try {
            Class.forName("com.mongodb.MongoClient");

            final Block<ClusterSettings.Builder> cluster = builder -> builder.hosts(
                Collections.singletonList(getServer(properties))
            );

            final MongoCredential credential = MongoCredential.createCredential(
              properties.getProperty("username"),
              properties.getProperty("database"),
              properties.getProperty("password").toCharArray()
            );

            final Block<ConnectionPoolSettings.Builder> connection = builder -> builder
              .minSize(1)
              .maxSize(30)
              .maxConnectionIdleTime(10, TimeUnit.MINUTES)
              .maxConnectionLifeTime(30, TimeUnit.MINUTES)
              .maxWaitTime(5, TimeUnit.MINUTES);

            final Block<SocketSettings.Builder> socket = builder -> builder
              .connectTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS);

            final PojoCodecProvider pojo = PojoCodecProvider.builder()
              .automatic(true)
              .build();

            final CodecRegistry codec = CodecRegistries.fromRegistries(
              MongoClientSettings.getDefaultCodecRegistry(),
              CodecRegistries.fromProviders(pojo)
            );

            final MongoClientSettings settings = MongoClientSettings.builder()
              .applyToClusterSettings(cluster)
              .credential(credential)
              .applyToConnectionPoolSettings(connection)
              .applyToSocketSettings(socket)
              .codecRegistry(codec)
              .retryWrites(true)
              .readPreference(ReadPreference.primaryPreferred())
              .build();

            final MapperOptions options = MapperOptions.builder()
              .enablePolymorphicQueries(true)
              .cacheClassLookups(true)
              .build();

            this.client = MongoClients.create(settings);
            this.datastore = Morphia.createDatastore(client, properties.getProperty("database"), options);

            datastore.ensureIndexes();
        } catch (Throwable throwable) {
            throw new DatabaseException(throwable);
        }

    }

    @Override @Synchronized
    public void shutdown() {
        client.close();
    }

    @Override
    public boolean isConnect() {
        return client != null;
    }

    @Override @NotNull
    public DatabaseType getType() {
        return DatabaseType.MONGODB;
    }

    @Override
    public MongoClient getClient() {
        return client;
    }

    private ServerAddress getServer(@NotNull final Properties properties) {
        return new ServerAddress(
          properties.getProperty("hostname"),
          Integer.parseInt(properties.getProperty("port"))
        );
    }

}
