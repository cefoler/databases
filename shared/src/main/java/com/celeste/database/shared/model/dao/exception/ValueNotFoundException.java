package com.celeste.database.shared.model.dao.exception;

import org.jetbrains.annotations.NotNull;

/**
 * The class {@code ValueNotFoundException} is a checked {@code Exception}
 * class that inherits the {@link DAOException} class
 * that is, it is an exception that needs to be catch.
 *
 * <p> The class is called when a database value is required
 * and that value does not exist or is not found.
 *
 * @since 3.0
 * @see DAOException
 */
public class ValueNotFoundException extends DAOException {

  /**
   * Constructs a new {@code ValueNotFoundException} with the specified detail message.
   * The cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param error the detail message. The detail message is saved for
   * later retrieval by the {@link #getMessage()} method.
   */
  public ValueNotFoundException(@NotNull final String error) {
    super(error);
  }

  public ValueNotFoundException(@NotNull final Throwable cause) {
    super(cause);
  }

  public ValueNotFoundException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}