package com.celeste.databases.storage.model.database.provider.impl.sql.h2;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.DatabaseType;
import com.celeste.databases.core.model.entity.LocalCredentials;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import java.io.File;
import java.sql.Connection;
import java.util.Properties;
import org.h2.jdbc.JdbcConnection;
import org.jetbrains.annotations.NotNull;

public class H2Provider implements Sql {

  private static final String connectionUrl;

  static {
    connectionUrl = "jdbc:h2:{path}";
  }

  private final LocalCredentials credentials;
  private Connection connection;

  public H2Provider(@NotNull final LocalCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      Class.forName("org.h2.jdbc.JdbcConnection");

      final String name = credentials.getName();
      final File path = credentials.getPath();

      this.connection = new JdbcConnection(
          connectionUrl.replace("{path}", path.getAbsolutePath()),
          new Properties()
      );
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  public synchronized void shutdown() {

  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public @NotNull DatabaseType getDatabaseType() {
    return null;
  }
}
