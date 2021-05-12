package com.celeste.databases.core.model.database.exception;

public class InvalidCredentialsException extends DatabaseException {

  public InvalidCredentialsException(final String error) {
    super(error);
  }

  public InvalidCredentialsException(final Throwable cause) {
    super(cause);
  }

  public InvalidCredentialsException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
