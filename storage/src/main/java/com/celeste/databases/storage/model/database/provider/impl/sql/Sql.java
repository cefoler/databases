package com.celeste.databases.storage.model.database.provider.impl.sql;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.model.database.provider.Storage;
import java.sql.Connection;

public interface Sql extends Storage {

  Connection getConnection() throws FailedConnectionException;

}
