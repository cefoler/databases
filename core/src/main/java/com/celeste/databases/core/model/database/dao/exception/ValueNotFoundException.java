package com.celeste.databases.core.model.database.dao.exception;

public class ValueNotFoundException extends DaoException {

  public ValueNotFoundException(final String error) {
    super(error);
  }

  public ValueNotFoundException(final Throwable cause) {
    super(cause);
  }

  public ValueNotFoundException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
