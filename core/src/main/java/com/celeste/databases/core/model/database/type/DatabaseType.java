package com.celeste.databases.core.model.database.type;

import com.google.common.collect.ImmutableList;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum DatabaseType {

  STORAGE("STORAGE", "STGE"),
  MESSENGER("MESSENGER", "MSG"),
  CACHE("CACHE", "CH");

  private final List<String> names;

  DatabaseType(final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  public static DatabaseType getDatabase(final String database) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(database.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid database: " + database));
  }

  public static DatabaseType getDatabase(final String database,
      @Nullable final DatabaseType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(database.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}