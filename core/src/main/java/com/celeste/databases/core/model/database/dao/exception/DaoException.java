package com.celeste.databases.core.model.database.dao.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class DaoException extends DatabaseException {

  public DaoException(@NotNull final String error) {
    super(error);
  }

  public DaoException(@NotNull final Throwable cause) {
    super(cause);
  }

  public DaoException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
