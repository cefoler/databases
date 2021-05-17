package com.celeste.database.shared.exception.dao;

import com.celeste.database.shared.exception.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

/**
 * The class {@code DAOException} is a checked {@code Exception} class that inherits the
 * {@link DatabaseException} class that is, it is an exception that needs to be catch.
 *
 * <p> The class is called when the creation of the DAO of the database
 * failed</p>
 *
 * @see DatabaseException
 * @since 3.0
 */
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