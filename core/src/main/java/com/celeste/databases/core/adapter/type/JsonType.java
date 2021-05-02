package com.celeste.databases.core.adapter.type;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum JsonType {

  JACKSON("JACKSON"),
  GSON("GSON");

  private final List<String> names;

  JsonType(@NotNull final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  @NotNull
  public static JsonType getJson(@NotNull final String json) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(json.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Invalid json: " + json));
  }

}