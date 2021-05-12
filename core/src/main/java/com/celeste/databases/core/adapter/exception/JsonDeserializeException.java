package com.celeste.databases.core.adapter.exception;

public class JsonDeserializeException extends JsonException {

  public JsonDeserializeException(final String error) {
    super(error);
  }

  public JsonDeserializeException(final Throwable cause) {
    super(cause);
  }

  public JsonDeserializeException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
