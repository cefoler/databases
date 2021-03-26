package com.celeste.shared.model.database.provider.exception;

import org.jetbrains.annotations.NotNull;

public class FailedConnectionException extends DatabaseException {

  public FailedConnectionException(@NotNull final String error) {
    super(error);
  }

  public FailedConnectionException(@NotNull final Throwable cause) {
    super(cause);
  }

  public FailedConnectionException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}