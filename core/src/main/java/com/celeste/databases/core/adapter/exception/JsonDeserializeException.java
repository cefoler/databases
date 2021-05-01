package com.celeste.databases.core.adapter.exception;

import org.jetbrains.annotations.NotNull;

public class JsonDeserializeException extends JsonException {

  public JsonDeserializeException(@NotNull final String error) {
    super(error);
  }

  public JsonDeserializeException(@NotNull final Throwable cause) {
    super(cause);
  }

  public JsonDeserializeException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
