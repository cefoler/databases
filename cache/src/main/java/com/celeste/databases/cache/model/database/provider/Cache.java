package com.celeste.databases.cache.model.database.provider;

import com.celeste.databases.cache.model.database.dao.CacheDao;
import com.celeste.databases.cache.model.database.type.CacheType;
import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.type.DatabaseType;

public interface Cache extends Database {

  CacheType getCacheType();

  CacheDao createDao();

  @Override
  default DatabaseType getDatabaseType() {
    return DatabaseType.CACHE;
  }

}
