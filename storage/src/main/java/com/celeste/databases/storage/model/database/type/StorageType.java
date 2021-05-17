package com.celeste.databases.storage.model.database.type;

import com.celeste.databases.core.model.database.type.AccessType;
import com.celeste.databases.storage.model.database.provider.Storage;
import com.celeste.databases.storage.model.database.provider.impl.mongodb.MongoDbProvider;
import com.celeste.databases.storage.model.database.provider.impl.sql.flat.h2.H2Provider;
import com.celeste.databases.storage.model.database.provider.impl.sql.flat.sqlite.SqLiteProvider;
import com.celeste.databases.storage.model.database.provider.impl.sql.mysql.MySqlProvider;
import com.celeste.databases.storage.model.database.provider.impl.sql.postgresql.PostgreSqlProvider;
import com.google.common.collect.ImmutableList;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum StorageType {

  MONGODB(MongoDbProvider.class, AccessType.REMOTE, "MONGODB", "MONGOD", "MONGO"),
  MYSQL(MySqlProvider.class, AccessType.REMOTE, "MYSQL", "SQL"),
  POSTGRESQL(PostgreSqlProvider.class, AccessType.REMOTE, "POSTGRESQL", "POSTGRE", "POST"),
  H2(H2Provider.class, AccessType.LOCAL, "H2"),
  SQLITE(SqLiteProvider.class, AccessType.LOCAL, "SQLITE", "SQLT");

  private final Class<? extends Storage> provider;
  private final AccessType access;
  private final List<String> names;

  StorageType(final Class<? extends Storage> provider, final AccessType access,
      final String... names) {
    this.provider = provider;
    this.access = access;
    this.names = ImmutableList.copyOf(names);
  }

  public static StorageType getStorage(final String storage) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(storage.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid storage: " + storage));
  }

  public static StorageType getStorage(final String storage, @Nullable final StorageType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(storage.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}
