package com.celeste.databases.core.adapter.type;

import com.google.common.collect.ImmutableList;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum JsonType {

  JACKSON("JACKSON"),
  GSON("GSON");

  private final List<String> names;

  JsonType(final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  public static JsonType getJson(final String json) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(json.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid json: " + json));
  }

  public static JsonType getStorage(final String json, @Nullable final JsonType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(json.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}