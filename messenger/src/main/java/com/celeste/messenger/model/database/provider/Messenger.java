package com.celeste.messenger.model.database.provider;

import com.celeste.shared.model.dao.exception.DAOException;
import com.celeste.shared.model.database.provider.Database;
import com.celeste.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.shared.model.database.type.DatabaseType;
import com.celeste.messenger.model.dao.MessengerDAO;
import com.celeste.messenger.model.database.type.MessengerType;
import org.jetbrains.annotations.NotNull;

public interface Messenger<U> extends Database {

  @NotNull
  MessengerType getCacheType();

  @NotNull
  U getConnection() throws FailedConnectionException;

  @NotNull
  MessengerDAO createDAO() throws DAOException, FailedConnectionException;

  @Override @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.MESSENGER;
  }

}