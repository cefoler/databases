package com.celeste.databases.core.model.database.exception;

import org.jetbrains.annotations.NotNull;

public class DatabaseException extends Exception {

  public DatabaseException(@NotNull final String error) {
    super(error);
  }

  public DatabaseException(@NotNull final Throwable cause) {
    super(cause);
  }

  public DatabaseException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
