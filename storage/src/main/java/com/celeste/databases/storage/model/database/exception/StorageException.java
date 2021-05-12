package com.celeste.databases.storage.model.database.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;

public class StorageException extends DatabaseException {

  public StorageException(final String error) {
    super(error);
  }

  public StorageException(final Throwable cause) {
    super(cause);
  }

  public StorageException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
