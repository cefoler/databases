package com.celeste.databases.core.model.database.exception;

public class DatabaseException extends Exception {

  public DatabaseException(final String error) {
    super(error);
  }

  public DatabaseException(final Throwable cause) {
    super(cause);
  }

  public DatabaseException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
