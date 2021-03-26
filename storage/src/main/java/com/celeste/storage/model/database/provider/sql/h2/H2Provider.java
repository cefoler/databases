package com.celeste.storage.model.database.provider.sql.h2;

import com.celeste.shared.model.dao.exception.DAOException;
import com.celeste.shared.model.dao.exception.ValueNotFoundException;
import com.celeste.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.storage.model.database.provider.sql.SQL;
import com.celeste.storage.model.database.type.StorageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.h2.jdbc.JdbcConnection;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Getter(AccessLevel.PRIVATE)
public final class H2Provider implements SQL {

  private final Properties properties;
  private final String connectionUrl;

  private Connection connection;

  public H2Provider(@NotNull final Properties properties) throws FailedConnectionException {
    this.properties = properties;
    this.connectionUrl = "jdbc:h2:{path}";

    init();
  }

  @Override @Synchronized
  public void init() throws FailedConnectionException {
    try {
      Class.forName("org.h2.jdbc.JdbcConnection");

      final String name = properties.getProperty("name");
      final Path path = Paths.get(properties.getProperty("path")).resolve(name);

      this.connection = new JdbcConnection(
          connectionUrl.replace("{path}", path.toString()),
          new Properties()
      );
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override @Synchronized @SneakyThrows
  public void shutdown() {
    connection.close();
  }

  @Override @SneakyThrows
  public boolean isClose() {
    return connection.isClosed();
  }

  @Override @NotNull
  public StorageType getStorageType() {
    return StorageType.H2;
  }

  @Override
  public int executeUpdate(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try (final PreparedStatement statement = getConnection().prepareStatement(sql
        .replace("REPLACE", "MERGE")
    )) {
      applyValues(statement, values);
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @Override @NotNull
  public ResultSet executeQuery(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try {
      final PreparedStatement statement = getConnection().prepareStatement(sql);

      applyValues(statement, values);
      return statement.executeQuery();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @Override @NotNull
  public Connection getConnection() throws FailedConnectionException {
    if (connection == null) throw new FailedConnectionException("Connection has been closed");

    return connection;
  }

}