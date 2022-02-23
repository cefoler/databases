package com.celeste.databases.storage.model.database.provider.impl.sql.flat.sqlite;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.LocalCredentials;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import com.celeste.databases.storage.model.database.type.StorageType;
import com.celeste.databases.storage.model.entity.impl.NonClosableConnection;
import java.sql.Connection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.jdbc4.JDBC4Connection;

public final class SqLiteProvider implements Sql {

  private static final String URI;
  private static final String FILE;

  static {
    URI = "jdbc:h2:<path>";
    FILE = "<name>.db";
  }

  private final LocalCredentials credentials;
  private NonClosableConnection connection;

  public SqLiteProvider(final LocalCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      Class.forName("org.sqlite.jdbc4.JDBC4Connection");

      final String newUri = URI.replace("<path>", credentials.getPath().getAbsolutePath());
      final String newFile = FILE.replace("<name>", credentials.getName());

      final Connection connection = new JDBC4Connection(newUri, newFile, new Properties());
      this.connection = new NonClosableConnection(connection);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      connection.shutdown();
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    try {
      return connection.isClosed();
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public StorageType getStorageType() {
    return StorageType.SQLITE;
  }

  @Override
  public NonClosableConnection getConnection() {
    return connection;
  }

}
