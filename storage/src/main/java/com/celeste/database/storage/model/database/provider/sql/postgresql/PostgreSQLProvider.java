package com.celeste.database.storage.model.database.provider.sql.postgresql;

import com.celeste.database.shared.model.dao.exception.ValueNotFoundException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.storage.model.database.provider.sql.SQL;
import com.celeste.database.storage.model.database.type.StorageType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PRIVATE)
public final class PostgreSQLProvider implements SQL {

  private final Properties properties;

  private HikariDataSource hikari;

  public PostgreSQLProvider(@NotNull final Properties properties) throws FailedConnectionException {
    this.properties = properties;

    init();
  }

  @Override @Synchronized
  public void init() throws FailedConnectionException {
    try {
      final HikariConfig config = new HikariConfig();

      config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");

      config.addDataSourceProperty("serverName", properties.getProperty("hostname"));
      config.addDataSourceProperty("portNumber", properties.getProperty("port"));
      config.addDataSourceProperty("databaseName", properties.getProperty("database"));
      config.addDataSourceProperty("user", properties.getProperty("username"));
      config.addDataSourceProperty("password", properties.getProperty("password"));

      config.setMinimumIdle(1);
      config.setMaximumPoolSize(20);

      config.setConnectionTimeout(30000);
      config.setIdleTimeout(600000);
      config.setMaxLifetime(1800000);

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
      config.addDataSourceProperty("alwaysSendSetIsolation", "false");
      config.addDataSourceProperty("cacheCallableStmts", "true");
      config.addDataSourceProperty("serverTimezone", "UTC");
      config.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

      this.hikari = new HikariDataSource(config);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override @Synchronized
  public void shutdown() {
    hikari.close();
  }

  @Override
  public boolean isClose() {
    return hikari.isClosed();
  }

  @Override @NotNull
  public StorageType getStorageType() {
    return StorageType.POSTGRESQL;
  }

  @Override
  public int executeUpdate(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try (
        final Connection connection = getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @Override @NotNull
  public ResultSet executeQuery(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try (
        final Connection connection = getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeQuery();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @Override @NotNull
  public Connection getConnection() throws FailedConnectionException {
    try {
      return hikari.getConnection();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }
  }

}