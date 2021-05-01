package com.celeste.databases.core.adapter.exception;

import org.jetbrains.annotations.NotNull;

public class JsonException extends Exception {

  public JsonException(@NotNull final String error) {
    super(error);
  }

  public JsonException(@NotNull final Throwable cause) {
    super(cause);
  }

  public JsonException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
