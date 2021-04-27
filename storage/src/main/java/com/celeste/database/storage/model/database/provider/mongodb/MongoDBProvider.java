package com.celeste.database.storage.model.database.provider.mongodb;

import com.celeste.database.shared.exception.database.FailedConnectionException;
import com.celeste.database.storage.model.database.StorageType;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;

@Getter(AccessLevel.PRIVATE)
public final class MongoDBProvider implements MongoDB {

  private final Properties properties;

  private MongoClient client;
  private MongoDatabase database;

  public MongoDBProvider(@NotNull final Properties properties) throws FailedConnectionException {
    this.properties = properties;

    init();
  }

  @Override
  @Synchronized
  public void init() throws FailedConnectionException {
    try {
      Class.forName("com.mongodb.client.MongoClients");

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

      if (properties.contains("url")) {
        final ConnectionString url = new ConnectionString(properties.getProperty("url"));
        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(url)
            .applyToConnectionPoolSettings(connection)
            .applyToSocketSettings(socket)
            .codecRegistry(codec)
            .retryWrites(true)
            .readPreference(ReadPreference.primaryPreferred())
            .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(properties.getProperty("database"));
        return;
      }

      final Block<ClusterSettings.Builder> cluster = builder -> builder.hosts(
          Arrays.asList(getServer(properties))
      );

      final MongoCredential credential = MongoCredential.createCredential(
          properties.getProperty("username"),
          properties.getProperty("authentication"),
          properties.getProperty("password").toCharArray()
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

      this.client = MongoClients.create(settings);
      this.database = client.getDatabase(properties.getProperty("database"));
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  @Synchronized
  public void shutdown() {
    client.close();
  }

  @Override
  public boolean isClosed() {
    return client == null;
  }

  @Override
  @NotNull
  public StorageType getStorageType() {
    return StorageType.MONGODB;
  }

  @Override
  @NotNull
  public MongoClient getClient() throws FailedConnectionException {
    if (client == null) {
      throw new FailedConnectionException("Connection has been closed");
    }

    return client;
  }

  @Override
  @NotNull
  public MongoDatabase getDatabase() throws FailedConnectionException {
    if (database == null) {
      throw new FailedConnectionException("Connection has been closed");
    }

    return database;
  }

  @NotNull
  private ServerAddress getServer(@NotNull final Properties properties) {
    return new ServerAddress(
        properties.getProperty("hostname"),
        Integer.parseInt(properties.getProperty("port"))
    );
  }

}