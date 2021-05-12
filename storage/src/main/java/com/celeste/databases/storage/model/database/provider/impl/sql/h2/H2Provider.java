package com.celeste.databases.storage.model.database.provider.impl.sql.h2;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.LocalCredentials;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import com.celeste.databases.storage.model.entity.impl.NonClosableConnection;
import java.sql.Connection;
import java.sql.SQLException;
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

      final String name = credentials.getName();
      final String path = credentials.getPath().getAbsolutePath();

      final String newUri = NAME.matcher(PATH.matcher(URI)
          .replaceAll(Matcher.quoteReplacement(path)))
          .replaceAll(Matcher.quoteReplacement(name));
      final Properties properties = new Properties();

      final Connection closableConnection = new JdbcConnection(newUri, properties);
      this.connection = new NonClosableConnection(closableConnection);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      connection.shutdown();
    } catch (SQLException exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() throws FailedConnectionException{
    try {
      return connection.isClosed();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
