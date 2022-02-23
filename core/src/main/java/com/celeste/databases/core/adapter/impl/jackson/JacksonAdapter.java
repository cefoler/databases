package com.celeste.databases.core.adapter.impl.jackson;

import com.celeste.databases.core.adapter.Json;
import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.exception.JsonSerializeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;

public final class JacksonAdapter implements Json {

  private static final JacksonAdapter INSTANCE;

  static {
    INSTANCE = new JacksonAdapter();
  }

  private final ObjectMapper mapper;

  private JacksonAdapter() {
    this.mapper = new ObjectMapper();
  }

  @Override
  public String serialize(final Object value) throws JsonSerializeException {
    try {
      return mapper.writeValueAsString(value);
    } catch (Exception exception) {
      throw new JsonSerializeException(exception);
    }
  }

  @Override
  public <T> T deserialize(final String json, final Class<T> clazz)
      throws JsonDeserializeException {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception exception) {
      throw new JsonDeserializeException(exception);
    }
  }

  public ObjectNode deserialize(final URL url)
      throws JsonDeserializeException {
    try {
      return mapper.readValue(url, ObjectNode.class);
    } catch (Exception exception) {
      throw new JsonDeserializeException(exception);
    }
  }

  public static JacksonAdapter getInstance() {
    return INSTANCE;
  }

}
