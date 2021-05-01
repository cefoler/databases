package com.celeste.databases.core.model.database.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidCredentialsException extends DatabaseException {

  public InvalidCredentialsException(@NotNull final String error) {
    super(error);
  }

  public InvalidCredentialsException(@NotNull final Throwable cause) {
    super(cause);
  }

  public InvalidCredentialsException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
