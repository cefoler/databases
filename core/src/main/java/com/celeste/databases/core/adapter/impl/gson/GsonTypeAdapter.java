package com.celeste.databases.core.adapter.impl.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public final class GsonTypeAdapter<T> implements TypeAdapterFactory {

  private final Class<T> base;

  private final String fieldName;

  private final Map<String, Class<?>> labelToSubtype;
  private final Map<Class<?>, String> subtypeToLabel;

  private final boolean maintain;

  public GsonTypeAdapter(final Class<T> base) {
    this(base, "type");
  }

  public GsonTypeAdapter(final Class<T> base, final String fieldName) {
    this(base, fieldName, false);
  }

  public GsonTypeAdapter(final Class<T> base, final String fieldName,
      final boolean maintain) {
    this.base = base;
    this.fieldName = fieldName;
    this.maintain = maintain;
    this.labelToSubtype = new LinkedHashMap<>();
    this.subtypeToLabel = new LinkedHashMap<>();
  }

  public GsonTypeAdapter<T> registerSubtype(final Class<? extends T> subtype, final String label) {
    if (subtypeToLabel.containsKey(subtype) || labelToSubtype.containsKey(label)) {
      throw new IllegalArgumentException("Types and labels must be unique");
    }

    labelToSubtype.put(label, subtype);
    subtypeToLabel.put(subtype, label);

    return this;
  }

  public GsonTypeAdapter<T> registerSubtype(final Class<? extends T> subType) {
    return registerSubtype(subType, subType.getSimpleName());
  }

  @SafeVarargs
  public final GsonTypeAdapter<T> registerSubtypes(final Class<? extends T>... subTypes) {
    Arrays.stream(subTypes).forEach(this::registerSubtype);
    return this;
  }

  @Nullable
  @SuppressWarnings("ObjectAllocationInLoop")
  public <U> TypeAdapter<U> create(final Gson gson, final TypeToken<U> token) {
    if (token.getRawType() != base) {
      return null;
    }

    final Map<String, TypeAdapter<?>> labels = new LinkedHashMap<>();
    final Map<Class<?>, TypeAdapter<?>> subtypes = new LinkedHashMap<>();

    for (final Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
      final TypeToken<?> newToken = TypeToken.get(entry.getValue());
      final TypeAdapter<?> type = gson.getDelegateAdapter(this, newToken);

      labels.put(entry.getKey(), type);
      subtypes.put(entry.getValue(), type);
    }

    return new TypeAdapter<U>() {

      @Override
      public U read(final JsonReader reader) {
        final JsonElement json = Streams.parse(reader);
        final JsonElement newJson = maintain
            ? json.getAsJsonObject().get(fieldName)
            : json.getAsJsonObject().remove(fieldName);

        if (newJson == null) {
          throw new JsonParseException("Cannot deserialize " + base
              + ". It doesn't define a Field named " + fieldName);
        }

        final String label = newJson.getAsString();
        final TypeAdapter<U> type = (TypeAdapter<U>) labels.get(label);

        if (type == null) {
          throw new JsonParseException("Cannot deserialize " + base + " on SubType named " + label);
        }

        return type.fromJsonTree(json);
      }

      @Override
      public void write(final JsonWriter writer, final U value) throws IOException {
        final Class<?> clazz = value.getClass();

        final String label = subtypeToLabel.get(clazz);
        final TypeAdapter<U> type = (TypeAdapter<U>) subtypes.get(clazz);

        if (type == null) {
          throw new JsonParseException("Cannot serialize " + clazz.getName());
        }

        final JsonObject json = type.toJsonTree(value).getAsJsonObject();

        if (maintain) {
          Streams.write(json, writer);
          return;
        }

        if (json.has(fieldName)) {
          throw new JsonParseException("Cannot serialize " + clazz.getName()
              + ". It already defines a Field named " + fieldName);
        }

        final JsonObject newJson = new JsonObject();
        newJson.add(fieldName, new JsonPrimitive(label));

        for (final Entry<String, JsonElement> entry : json.entrySet()) {
          newJson.add(entry.getKey(), entry.getValue());
        }

        Streams.write(newJson, writer);
      }
    }.nullSafe();

  }

}
