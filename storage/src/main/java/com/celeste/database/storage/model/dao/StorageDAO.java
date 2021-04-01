package com.celeste.database.storage.model.dao;

import com.celeste.database.shared.model.dao.DAO;
import com.celeste.database.shared.model.dao.exception.ValueNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StorageDAO<T> extends DAO {

  void createTable();

  void save(@NotNull final T... values);

  void delete(@NotNull final Object key);

  boolean contains(@NotNull final Object key);

  @NotNull
  T find(@NotNull final Object key) throws ValueNotFoundException;

  @NotNull
  List<T> findAll();

}