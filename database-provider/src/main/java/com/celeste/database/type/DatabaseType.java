package com.celeste.database.type;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DatabaseType {

    MYSQL("MYSQL"),
    MONGODB("MONGODB", "MONGO");

    @NotNull
    private final List<String> names;

    DatabaseType(@NotNull final String... names) {
        this.names = Arrays.asList(names);
    }

    @NotNull
    public static DatabaseType getDatabase(@NotNull final String database) {
        return Arrays.stream(values())
          .filter(type -> type.getNames().contains(database.toUpperCase()))
          .findFirst()
          .orElse(MYSQL);
    }

}