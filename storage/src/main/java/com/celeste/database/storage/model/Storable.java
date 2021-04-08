package com.celeste.database.storage.model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Storable is a serializable interface used in Object models
 * to implement the Read and Write methods, used in the SQL databases.
 *
 * <p>If you are using MongoDB, this methods should be implemented
 * but returned null</p>
 *
 * @param <T> Object
 */
public interface Storable<T> extends Serializable {

  @NotNull
  T read(@NotNull final ResultSet result) throws SQLException, IOException;

  @NotNull
  Object[] write(@NotNull final T value) throws SQLException, IOException;

}