package com.celeste.databases.core.adapter.exception;

import org.jetbrains.annotations.NotNull;

public class JsonSerializeException extends JsonException {

  public JsonSerializeException(@NotNull final String error) {
    super(error);
  }

  public JsonSerializeException(@NotNull final Throwable cause) {
    super(cause);
  }

  public JsonSerializeException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
