package com.celeste.databases.storage.model.database.exception;

import com.celeste.databases.core.model.database.exception.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class StorageException extends DatabaseException {

  public StorageException(@NotNull final String error) {
    super(error);
  }

  public StorageException(@NotNull final Throwable cause) {
    super(cause);
  }

  public StorageException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
