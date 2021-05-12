package com.celeste.databases.core.adapter.exception;

public class JsonException extends Exception {

  public JsonException(final String error) {
    super(error);
  }

  public JsonException(final Throwable cause) {
    super(cause);
  }

  public JsonException(final String error, final Throwable cause) {
    super(error, cause);
  }

}
