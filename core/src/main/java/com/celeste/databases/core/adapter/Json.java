package com.celeste.databases.core.adapter;

import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.exception.JsonSerializeException;
import org.jetbrains.annotations.NotNull;

public interface Json {

  @NotNull
  String serialize(@NotNull final Object value) throws JsonSerializeException;

  @NotNull
  <T> T deserialize(@NotNull final String json, @NotNull final Class<T> clazz)
      throws JsonDeserializeException;

}
