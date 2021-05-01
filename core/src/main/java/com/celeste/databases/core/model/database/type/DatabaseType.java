package com.celeste.databases.core.model.database.type;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum DatabaseType {

  STORAGE("STORAGE", "STGE"),
  MESSENGER("MESSENGER", "MSG"),
  CACHE("CACHE", "CH");

  private final List<String> names;

  DatabaseType(@NotNull final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  @NotNull
  public static DatabaseType getDatabase(@NotNull final String database) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(database.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Invalid database: " + database));
  }

}