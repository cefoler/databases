package com.celeste.databases.core.model.database.dao.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;

public class DaoException extends DatabaseException {

  public DaoException(final String error) {
    super(error);
  }

  public DaoException(final Throwable cause) {
    super(cause);
  }

  public DaoException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
