package com.celeste.databases.storage.model.database.dao;

import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.model.entity.Data;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractStorageDao<T extends Database, U> implements StorageDao<U> {

  protected final T storage;
  protected final Class<U> clazz;
  protected final Data<U> data;

  public AbstractStorageDao(final T storage, final Class<U> clazz) {
    this.storage = storage;
    this.clazz = clazz;
    this.data = new Data<>(clazz);
  }

  protected Class<U> getClazz() {
    return clazz;
  }

  protected Data<U> getData() {
    return data;
  }

  @Override
  public T getDatabase() {
    return storage;
  }

  @Override
  public CompletableFuture<Void> saveAsync(final U... entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        save(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public CompletableFuture<Void> saveAsync(final Collection<U> entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        save(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public CompletableFuture<Void> deleteAsync(final U... entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        delete(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public CompletableFuture<Void> deleteAsync(final Collection<U> entities) {
    return CompletableFuture.runAsync(() -> {
      try {
        delete(entities);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public CompletableFuture<Boolean> containsAsync(final Object key) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return contains(key);
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }

      return null;
    });
  }

  @Override
  public CompletableFuture<U> findAsync(final Object key) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return find(key);
      } catch (FailedConnectionException | ValueNotFoundException exception) {
        exception.printStackTrace();
      }

      return null;
    });
  }

  @Override
  public CompletableFuture<List<U>> findAllAsync() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return findAll();
      } catch (FailedConnectionException exception) {
        exception.printStackTrace();
      }

      return null;
    });
  }

}
