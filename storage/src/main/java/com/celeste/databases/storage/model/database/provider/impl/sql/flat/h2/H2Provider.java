package com.celeste.databases.storage.model.database.provider.impl.sql.flat.h2;

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
import org.h2.jdbc.JdbcConnection;

public final class H2Provider implements Sql {

  private static final String URI;

  private static final Pattern PATH;
  private static final Pattern NAME;

  static {
    URI = "jdbc:h2:<path>/<name>";

    PATH = Pattern.compile("<path>", Pattern.LITERAL);
    NAME = Pattern.compile("<name>", Pattern.LITERAL);
  }

  private final LocalCredentials credentials;
  private NonClosableConnection connection;

  public H2Provider(final LocalCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      Class.forName("org.h2.jdbc.JdbcConnection");

      final String newUri = NAME.matcher(PATH.matcher(URI)
          .replaceAll(Matcher.quoteReplacement(credentials.getPath().getAbsolutePath())))
          .replaceAll(Matcher.quoteReplacement(credentials.getName()));

      final Connection closableConnection = new JdbcConnection(newUri, new Properties());
      this.connection = new NonClosableConnection(closableConnection);
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
    return StorageType.H2;
  }

  @Override
  public NonClosableConnection getConnection() {
    return connection;
  }

}
