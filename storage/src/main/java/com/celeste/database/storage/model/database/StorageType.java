package com.celeste.database.storage.model.database;

import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.database.provider.mongodb.MongoDBProvider;
import com.celeste.database.storage.model.database.provider.sql.h2.H2Provider;
import com.celeste.database.storage.model.database.provider.sql.mysql.MySQLProvider;
import com.celeste.database.storage.model.database.provider.sql.postgresql.PostgreSQLProvider;
import com.celeste.database.storage.model.database.provider.sql.sqlite.SQLiteProvider;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * The StorageType contains all possible types of database that this framework can access and
 * establish a connection.
 */
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

  /**
   * Get the storage by their name
   *
   * @param storage String
   * @return StorageType
   */
  public static StorageType getStorage(final String storage) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(storage.toUpperCase()))
        .findFirst()
        .orElse(SQLITE);
  }

}