package com.celeste.databases.core.adapter.impl.gson;

import com.celeste.databases.core.adapter.Json;
import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.exception.JsonSerializeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public final class GsonAdapter implements Json {

  private static final GsonAdapter INSTANCE = new GsonAdapter();
  private Gson gson;

  private GsonAdapter() {
    this.gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .create();
  }

  @SafeVarargs
  public final <T> void registerAdapter(@NotNull final Class<T> type,
      @NotNull final Class<? extends T>... subTypes) {
    final GsonTypeAdapter<T> adapter = new GsonTypeAdapter<>(type)
        .registerSubtypes(subTypes);

    gson = gson.newBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();
  }

  @Override
  @NotNull
  public String serialize(@NotNull final Object value) throws JsonSerializeException {
    try {
      final String json = gson.toJson(value);

      if (json == null) {
        throw new IOException("It was not possible to serialize the object to json");
      }

      return json;
    } catch (Throwable throwable) {
      throw new JsonSerializeException(throwable);
    }
  }

  @Override
  @NotNull
  public <T> T deserialize(@NotNull final String json, @NotNull final Class<T> clazz)
      throws JsonDeserializeException {
    try {
      final T instance = gson.fromJson(json, clazz);

      if (instance == null) {
        throw new IOException("Could not deserialize String for Object the class"
            + clazz.getSimpleName());
      }

      return instance;
    } catch (Throwable throwable) {
      throw new JsonDeserializeException(throwable);
    }
  }

  @NotNull
  public static GsonAdapter getInstance() {
    return INSTANCE;
  }

}
