package com.celeste.database.messenger.model.database.provider;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.shared.model.Database;
import com.celeste.database.shared.model.type.ConnectionType;
import com.celeste.database.shared.model.type.DatabaseType;
import com.celeste.database.messenger.model.database.MessengerType;
import org.jetbrains.annotations.NotNull;

public interface Messenger<U> extends Database {

  /**
   * Returns the Type of the Messenger
   * @return MessengerType
   */
  @NotNull
  MessengerType getType();

  /**
   * Returns the Connection type of the Messenger
   * @return ConnectionType
   */
  @NotNull
  ConnectionType getConnectionType();

  /**
   * Returns the instance of the connection of the Messenger
   *
   * @return U
   * @throws FailedConnectionException Throws when the connection has failed
   */
  @NotNull
  U getConnection() throws FailedConnectionException;

  /**
   * Creates a MessengerDAO
   * @return MessengerDAO
   *
   * @throws DAOException Throws when a error was caught during the creation of the DAO
   * @throws FailedConnectionException Throws when the connection has failed
   */
  @NotNull
  MessengerDAO createDAO() throws DAOException, FailedConnectionException;

  /**
   * Returns the DatabaseType, this is always MESSENGER by default.
   * @return DatabaseType
   */
  @Override @NotNull
  default DatabaseType getDatabaseType() {
    return DatabaseType.MESSENGER;
  }

}