package com.celeste.databases.cache.model.database.dao;

import com.celeste.databases.cache.model.database.provider.Cache;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCacheDao<T extends Cache> implements CacheDao {

  protected final T cache;

  @Override
  public T getDatabase() {
    return cache;
  }

}
