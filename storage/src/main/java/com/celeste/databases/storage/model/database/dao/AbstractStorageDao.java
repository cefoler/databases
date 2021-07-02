package com.celeste.databases.storage.model.database.dao;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.storage.model.entity.Entity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractStorageDao<T extends Database, U> implements StorageDao<U> {

  protected static final ExecutorService EXECUTOR;

  static {
    EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 5L, TimeUnit.MINUTES,
        new SynchronousQueue<>());
  }

  protected final T storage;
  protected final Class<U> clazz;
  protected final Entity<U> entity;

  public AbstractStorageDao(final T storage, final Class<U> clazz) {
    this.storage = storage;
    this.clazz = clazz;
    this.entity = new Entity<>(clazz);
  }

  protected Class<U> getClazz() {
    return clazz;
  }

  protected Entity<U> getEntity() {
    return entity;
  }

  @Override
  public T getDatabase() {
    return storage;
  }

}
