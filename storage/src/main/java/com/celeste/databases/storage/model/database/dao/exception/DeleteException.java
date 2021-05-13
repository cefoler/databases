package com.celeste.databases.storage.model.database.dao.exception;

import com.celeste.databases.core.model.database.dao.exception.DaoException;

public class DeleteException extends DaoException {

  public DeleteException(final String error) {
    super(error);
  }

  public DeleteException(final Throwable cause) {
    super(cause);
  }

  public DeleteException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
