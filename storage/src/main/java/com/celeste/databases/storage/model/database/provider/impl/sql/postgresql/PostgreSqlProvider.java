package com.celeste.databases.storage.model.database.provider.impl.sql.postgresql;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import com.celeste.databases.storage.model.database.type.StorageType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public final class PostgreSqlProvider implements Sql {

  private final RemoteCredentials credentials;
  private HikariDataSource hikari;

  public PostgreSqlProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      final HikariConfig config = new HikariConfig();

      config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");

      config.addDataSourceProperty("serverName", credentials.getHostname());
      config.addDataSourceProperty("portNumber", credentials.getPort());

      config.addDataSourceProperty("databaseName", credentials.getDatabase());

      config.addDataSourceProperty("user", credentials.getUsername());
      config.addDataSourceProperty("password", credentials.getPassword());

      config.addDataSourceProperty("sslmode", credentials.isSsl()
          ? "require"
          : "disable");

      config.setMinimumIdle(1);
      config.setMaximumPoolSize(20);

      config.setConnectionTimeout(30000);
      config.setIdleTimeout(600000);
      config.setMaxLifetime(1800000);

      config.addDataSourceProperty("alwaysSendSetIsolation", "false");
      config.addDataSourceProperty("autoReconnect", "true");
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.addDataSourceProperty("useServerPrepStmts", "true");
      config.addDataSourceProperty("useLocalSessionState", "true");
      config.addDataSourceProperty("rewriteBatchedStatements", "true");
      config.addDataSourceProperty("cacheResultSetMetadata", "true");
      config.addDataSourceProperty("cacheServerConfiguration", "true");
      config.addDataSourceProperty("elideSetAutoCommits", "true");
      config.addDataSourceProperty("maintainTimeStats", "false");
      config.addDataSourceProperty("cacheCallableStmts", "true");
      config.addDataSourceProperty("serverTimezone", "UTC");
      config.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

      this.hikari = new HikariDataSource(config);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      hikari.close();
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    try {
      return hikari.isClosed();
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public StorageType getStorageType() {
    return StorageType.POSTGRESQL;
  }

  @Override
  public Connection getConnection() throws FailedConnectionException {
    try {
      return hikari.getConnection();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
