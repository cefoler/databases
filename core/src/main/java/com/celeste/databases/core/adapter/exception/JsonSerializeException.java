package com.celeste.databases.core.adapter.exception;

public class JsonSerializeException extends JsonException {

  public JsonSerializeException(final String error) {
    super(error);
  }

  public JsonSerializeException(final Throwable cause) {
    super(cause);
  }

  public JsonSerializeException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
