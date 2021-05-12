package com.celeste.databases.core.adapter;

import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.exception.JsonSerializeException;

public interface Json {

  String serialize(final Object value) throws JsonSerializeException;

  <T> T deserialize(final String json, final Class<T> clazz) throws JsonDeserializeException;

}
