package com.celeste.databases.storage.model.entity.impl;

import com.celeste.databases.storage.model.entity.ForwardingConnection;
import java.sql.Connection;
import java.sql.SQLException;

public final class NonClosableConnection extends ForwardingConnection {

  public NonClosableConnection(final Connection connection) {
    super(connection);
  }

  @Override
  public void close() {
    // TODO: Method empty, as this connection cannot be closed by AutoCloseable.
  }

  public void shutdown() throws SQLException {
    final Connection connection = getConnection();
    connection.close();
  }

}
