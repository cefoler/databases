package com.celeste.databases.storage.model.database.dao.exception;

import com.celeste.databases.core.model.database.dao.exception.DaoException;

public class SaveException extends DaoException {

  public SaveException(final String error) {
    super(error);
  }

  public SaveException(final Throwable cause) {
    super(cause);
  }

  public SaveException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
