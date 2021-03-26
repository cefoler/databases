package com.celeste.storage.model.database.type;

import com.celeste.storage.model.database.provider.Storage;
import com.celeste.storage.model.database.provider.mongodb.MongoDBProvider;
import com.celeste.storage.model.database.provider.sql.h2.H2Provider;
import com.celeste.storage.model.database.provider.sql.mysql.MySQLProvider;
import com.celeste.storage.model.database.provider.sql.postgresql.PostgreSQLProvider;
import com.celeste.storage.model.database.provider.sql.sqlite.SQLiteProvider;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public enum StorageType {

  MONGODB(MongoDBProvider.class, "MONGODB", "MONGO"),
  MYSQL(MySQLProvider.class, "MYSQL"),
  POSTGRESQL(PostgreSQLProvider.class, "POSTGRESQL", "POSTGRE", "POST", "GRE"),
  H2(H2Provider.class, "H2"),
  SQLITE(SQLiteProvider.class, "SQLITE", "SQL", "LITE");

  @NotNull
  private final Class<? extends Storage> provider;

  @NotNull
  private final List<String> names;

  StorageType(@NotNull final Class<? extends Storage> provider, @NotNull final String... names) {
    this.provider = provider;
    this.names = ImmutableList.copyOf(names);
  }

  public static StorageType getStorage(@NotNull final String storage) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(storage.toUpperCase()))
        .findFirst()
        .orElse(SQLITE);
  }

}