package com.celeste.databases.messenger.model.database.provider;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.DatabaseType;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.type.MessengerType;

public interface Messenger extends Database {

  MessengerType getMessengerType();

  MessengerDao createDao() throws FailedConnectionException;

  @Override
  default DatabaseType getDatabaseType() {
    return DatabaseType.MESSENGER;
  }

}
