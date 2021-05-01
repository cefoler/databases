package com.celeste.databases.core.adapter.impl.jackson;

import com.celeste.databases.core.adapter.Json;
import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.exception.JsonSerializeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

public final class JacksonAdapter implements Json {

  private static final JacksonAdapter INSTANCE = new JacksonAdapter();
  private final ObjectMapper mapper;

  private JacksonAdapter() {
    this.mapper = new ObjectMapper();
  }

  @Override
  @NotNull
  public String serialize(@NotNull final Object value) throws JsonSerializeException {
    try {
      return mapper.writeValueAsString(value);
    } catch (Throwable throwable) {
      throw new JsonSerializeException(throwable);
    }
  }

  @Override
  @NotNull
  public <T> T deserialize(@NotNull final String json, @NotNull final Class<T> clazz)
      throws JsonDeserializeException {
    try {
      return mapper.readValue(json, clazz);
    } catch (Throwable throwable) {
      throw new JsonDeserializeException(throwable);
    }
  }

  @NotNull
  public static JacksonAdapter getInstance() {
    return INSTANCE;
  }

}
