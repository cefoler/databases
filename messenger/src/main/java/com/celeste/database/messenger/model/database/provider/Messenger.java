package com.celeste.database.messenger.model.database.provider;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.shared.model.Database;
import com.celeste.database.shared.model.type.ConnectionType;
import com.celeste.database.shared.model.type.DatabaseType;
import com.celeste.database.messenger.model.database.type.MessengerType;
import org.jetbrains.annotations.NotNull;

public interface Messenger<U> extends Database {

  @NotNull
  MessengerType getCacheType();

  @NotNull
  ConnectionType getConnectionType();

  @NotNull
  U getConnection() throws FailedConnectionException;

  @NotNull
  MessengerDAO createDAO() throws DAOException, FailedConnectionException;

  @Override @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.MESSENGER;
  }

}