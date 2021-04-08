package com.celeste.database.shared.exceptions.dao;

import com.celeste.database.shared.exceptions.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class DAOException extends DatabaseException {

  public DAOException(@NotNull final String error) {
    super(error);
  }

  public DAOException(@NotNull final Throwable cause) {
    super(cause);
  }

  public DAOException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}