package com.celeste.database.shared.model.database.type;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DatabaseType {

  STORAGE("STORAGE", "STGE"),
  MESSENGER("MESSENGER", "CACHE");

  @NotNull
  private final List<String> names;

  DatabaseType(@NotNull final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  @NotNull
  public static DatabaseType getDataBase(@NotNull final String database) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(database.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Invalid database: " + database));
  }

}