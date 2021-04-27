package com.celeste.database.shared.exception.database;

import org.jetbrains.annotations.NotNull;

/**
 * The class {@code FailedConnectionException} is a checked {@code Exception} class that inherits
 * the {@link DatabaseException} class that is, it is an exception that needs to be catch.
 *
 * <p> The class is called when the connection with the database
 * failed </p>
 *
 * @since 3.0
 */
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