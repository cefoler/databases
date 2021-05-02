package com.celeste.databases.storage.model.entity.impl;

import com.celeste.databases.storage.model.entity.ForwardingConnection;
import java.sql.Connection;
import org.jetbrains.annotations.NotNull;

public final class NonClosableConnection extends ForwardingConnection {

  public NonClosableConnection(@NotNull final Connection connection) {
    super(connection);
  }

  @Override
  public void close() {
    // TODO: Method empty, as this connection cannot be closed by AutoCloseable.
  }

}
