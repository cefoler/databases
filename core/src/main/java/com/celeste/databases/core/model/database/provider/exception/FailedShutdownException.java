package com.celeste.databases.core.model.database.provider.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;

public class FailedShutdownException extends DatabaseException {

  public FailedShutdownException(final String error) {
    super(error);
  }

  public FailedShutdownException(final Throwable cause) {
    super(cause);
  }

  public FailedShutdownException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
