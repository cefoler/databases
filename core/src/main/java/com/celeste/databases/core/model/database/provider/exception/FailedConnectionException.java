package com.celeste.databases.core.model.database.provider.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;

public class FailedConnectionException extends DatabaseException {

  public FailedConnectionException(final String error) {
    super(error);
  }

  public FailedConnectionException(final Throwable cause) {
    super(cause);
  }

  public FailedConnectionException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
