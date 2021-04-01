package com.celeste.database.storage.model.database.provider.mongodb;

import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.storage.model.database.type.StorageType;
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

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PRIVATE)
public final class MongoDBProvider implements MongoDB {

  private final Properties properties;

  private MongoClient client;
  private Datastore datastore;

  public MongoDBProvider(@NotNull final Properties properties) throws FailedConnectionException {
    this.properties = properties;

    init();
  }

  @Override @Synchronized
  public void init() throws FailedConnectionException {
    try {
      Class.forName("com.mongodb.client.MongoClients");

      final Block<ClusterSettings.Builder> cluster = builder -> builder.hosts(
          Arrays.asList(getServer(properties))
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
      throw new FailedConnectionException(throwable);
    }
  }

  @Override @Synchronized
  public void shutdown() {
    client.close();
  }

  @Override
  public boolean isClose() {
    return client == null;
  }

  @Override @NotNull
  public StorageType getStorageType() {
    return StorageType.MONGODB;
  }

  @Override @NotNull
  public MongoClient getClient() throws FailedConnectionException {
    if (client == null) throw new FailedConnectionException("Connection has been closed");

    return client;
  }

  @Override @NotNull
  public Datastore getDatastore() throws FailedConnectionException {
    if (datastore == null) throw new FailedConnectionException("Connection has been closed");

    return datastore;
  }

  @NotNull
  private ServerAddress getServer(@NotNull final Properties properties) {
    return new ServerAddress(
        properties.getProperty("hostname"),
        Integer.parseInt(properties.getProperty("port"))
    );
  }

}