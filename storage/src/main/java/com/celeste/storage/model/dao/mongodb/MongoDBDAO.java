package com.celeste.storage.model.dao.mongodb;

import com.celeste.shared.model.dao.exception.DAOException;
import com.celeste.shared.model.dao.exception.ValueNotFoundException;
import com.celeste.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.storage.model.dao.StorageDAO;
import com.celeste.storage.model.database.provider.mongodb.MongoDB;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter(AccessLevel.PRIVATE)
public final class MongoDBDAO<T> implements StorageDAO<T> {

  @Getter
  private final MongoDB database;

  private final Class<T> clazz;

  public MongoDBDAO(@NotNull final MongoDB database, @NotNull final Class<T> clazz) throws DAOException {
    try {
      this.database = database;
      this.clazz = clazz;
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }
  }

  @Override
  public void createTable() { }

  @Override @SafeVarargs @SneakyThrows
  public final void save(@NotNull final T... values) {
    database.getDatastore().save(values);
  }

  @Override @SneakyThrows
  public void delete(@NotNull final Object key) {
    database.getDatastore()
        .find(clazz)
        .filter(Filters.and(
            getFilters(key)
        ))
        .delete();
  }

  @Override @SneakyThrows
  public boolean contains(@NotNull final Object key) {
    return database.getDatastore()
        .find(clazz)
        .filter(Filters.and(
            getFilters(key)
        ))
        .count() > 0;
  }

  @Override @NotNull @SneakyThrows(FailedConnectionException.class)
  public T find(@NotNull final Object key) throws ValueNotFoundException {
    final T argument = database.getDatastore()
        .find(clazz)
        .filter(Filters.and(
            getFilters(key)
        ))
        .first();

    if (argument == null) throw new ValueNotFoundException("Value not found");

    return argument;
  }

  @Override @NotNull @SneakyThrows
  public List<T> findAll() {
    return database.getDatastore()
        .find(clazz)
        .iterator()
        .toList();
  }

  @NotNull
  private Filter[] getFilters(final Object key) {
    final List<Filter> filters = new ArrayList<>(Collections.singletonList(Filters.eq("_id", key)));

    if (key.toString().matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"))
      filters.add(Filters.eq("_id", UUID.fromString(key.toString())));

    return filters.toArray(new Filter[0]);
  }

}