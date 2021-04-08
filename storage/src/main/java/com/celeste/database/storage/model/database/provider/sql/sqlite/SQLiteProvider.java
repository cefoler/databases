package com.celeste.database.storage.model.database.provider.sql.sqlite;

import com.celeste.database.shared.exceptions.dao.ValueNotFoundException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.shared.model.type.ConnectionType;
import com.celeste.database.storage.model.database.provider.sql.SQL;
import com.celeste.database.storage.model.database.StorageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.jetbrains.annotations.NotNull;
import org.sqlite.jdbc4.JDBC4Connection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Getter(AccessLevel.PRIVATE)
public final class SQLiteProvider implements SQL {

  private final Properties properties;
  private final String connectionUrl;

  private Connection connection;

  public SQLiteProvider(@NotNull final Properties properties, final ConnectionType connectionType) throws FailedConnectionException {
    this.properties = properties;
    this.connectionUrl = "jdbc:sqlite:{path}";

    init();
  }

  @Override @Synchronized
  public void init() throws FailedConnectionException {
    try {
      Class.forName("org.sqlite.jdbc4.JDBC4Connection");

      final String name = properties.getProperty("name").concat(".db");
      final Path path = Paths.get(properties.getProperty("path")).resolve(name);

      this.connection = new JDBC4Connection(
          connectionUrl.replace("{path}", path.toString()),
          path.toString(),
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
  public boolean isClosed() {
    return connection.isClosed();
  }

  @Override @NotNull
  public StorageType getStorageType() {
    return StorageType.SQLITE;
  }

  @Override
  public int executeUpdate(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try (final PreparedStatement statement = getConnection().prepareStatement(sql)) {
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