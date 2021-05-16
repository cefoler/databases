package com.celeste.databases.storage.model.database.provider.impl.mongodb;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.storage.model.database.type.StorageType;
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
import com.mongodb.connection.SslSettings;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public final class MongoDbProvider implements MongoDb {

  private final RemoteCredentials credentials;

  private MongoClient client;
  private MongoDatabase database;

  public MongoDbProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public void init() throws FailedConnectionException {
    try {
      Class.forName("com.mongodb.client.MongoClients");
      Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

      final String hostname = credentials.getHostname();
      final int port = credentials.getPort();

      final String database = credentials.getDatabase();

      final String username = credentials.getUsername();
      final String password = credentials.getPassword();

      final boolean ssl = credentials.isSsl();

      final Properties properties = credentials.getOther();

      final String uri = properties.getProperty("uri", "");
      final String authentication = properties.getProperty("authentication", "admin");

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
          CodecRegistries.fromProviders(pojo));

      if (!uri.isEmpty()) {
        final ConnectionString connectionUri = new ConnectionString(uri);
        final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionUri)
            .applyToConnectionPoolSettings(connection)
            .applyToSocketSettings(socket)
            .codecRegistry(codec)
            .readPreference(ReadPreference.primaryPreferred())
            .retryWrites(true)
            .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(database);
        return;
      }

      final ServerAddress server = new ServerAddress(hostname, port);
      final Block<ClusterSettings.Builder> cluster = builder -> builder
          .hosts(Collections.singletonList(server));

      final MongoCredential credential = MongoCredential
          .createCredential(username, authentication, password.toCharArray());

      final Block<SslSettings.Builder> tls = builder -> builder.enabled(ssl);

      final MongoClientSettings settings = MongoClientSettings.builder()
          .applyToClusterSettings(cluster)
          .applyToConnectionPoolSettings(connection)
          .applyToSocketSettings(socket)
          .applyToSslSettings(tls)
          .codecRegistry(codec)
          .credential(credential)
          .readPreference(ReadPreference.primaryPreferred())
          .retryWrites(true)
          .build();

      this.client = MongoClients.create(settings);
      this.database = client.getDatabase(database);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getMessage(), exception.getCause());
    }
  }

  @Override
  public void shutdown() {
    client.close();
  }

  @Override
  public boolean isClosed() {
    try {
      client.listDatabases();
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  @Override
  public StorageType getStorageType() {
    return StorageType.MONGODB;
  }

  @Override
  public MongoClient getClient() {
    return client;
  }

  @Override
  public MongoDatabase getDatabase() {
    return database;
  }

}
