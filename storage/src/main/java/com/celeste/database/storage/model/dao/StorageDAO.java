package com.celeste.database.storage.model.dao;

import com.celeste.database.shared.exceptions.dao.ValueNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StorageDAO<T> {

  void createTable(final String name);

  void save(final Object key, @NotNull final T value);

  void delete(@NotNull final Object key);

  boolean contains(@NotNull final Object key);

  @NotNull
  T find(@NotNull final Object key) throws ValueNotFoundException;

  @NotNull
  List<T> findAll();

}