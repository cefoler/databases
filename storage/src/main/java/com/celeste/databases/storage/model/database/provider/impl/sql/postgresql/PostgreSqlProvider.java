package com.celeste.databases.storage.model.database.provider.impl.sql.postgresql;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.entity.RemoteCredentials;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
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

      final String hostname = credentials.getHostname();
      final int port = credentials.getPort();

      final String database = credentials.getDatabase();

      final String username = credentials.getUsername();
      final String password = credentials.getPassword();

      final boolean ssl = credentials.isSsl();

      config.addDataSourceProperty("serverName", hostname);
      config.addDataSourceProperty("portNumber", port);

      config.addDataSourceProperty("databaseName", database);

      config.addDataSourceProperty("user", username);
      config.addDataSourceProperty("password", password);

      config.addDataSourceProperty("sslmode", ssl ? "require" : "disable");

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
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  public synchronized void shutdown() {
    hikari.close();
  }

  @Override
  public boolean isClosed() {
    return hikari.isClosed();
  }

  @Override
  public Connection getConnection() throws FailedConnectionException {
    try {
      return hikari.getConnection();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
