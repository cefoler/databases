package com.celeste.databases.storage.model.database.dao.exception;

import com.celeste.databases.core.model.database.dao.exception.DaoException;

public class FindException extends DaoException {

  public FindException(final String error) {
    super(error);
  }

  public FindException(final Throwable cause) {
    super(cause);
  }

  public FindException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
