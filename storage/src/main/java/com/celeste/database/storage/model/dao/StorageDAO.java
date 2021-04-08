package com.celeste.database.storage.model.dao;

import com.celeste.database.shared.exceptions.dao.ValueNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StorageDAO<T> {

  /**
   * Creates the table in the database
   * @param name String
   */
  void createTable(final String name);

  /**
   * Saves a value in the database with the
   * provided key
   * @param key Object
   * @param value T
   */
  void save(final Object key, @NotNull final T value);

  /**
   * Deletes the value through the key provided
   * @param key Object
   */
  void delete(@NotNull final Object key);

  /**
   * Check if the value exists by the key
   * provided
   * @param key Object
   *
   * @return Boolean
   */
  boolean contains(@NotNull final Object key);

  /**
   * Get the value by the provided key
   * @param key Object
   *
   * @return T
   * @throws ValueNotFoundException Throws if the value wasn't found
   */
  @NotNull
  T find(@NotNull final Object key) throws ValueNotFoundException;

  /**
   * Get all objects from the database
   * @return List
   */
  @NotNull
  List<T> findAll();

}