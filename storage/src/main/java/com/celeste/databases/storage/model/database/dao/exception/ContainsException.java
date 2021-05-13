package com.celeste.databases.storage.model.database.dao.exception;

import com.celeste.databases.core.model.database.dao.exception.DaoException;

public class ContainsException extends DaoException {

  public ContainsException(final String error) {
    super(error);
  }

  public ContainsException(final Throwable cause) {
    super(cause);
  }

  public ContainsException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
