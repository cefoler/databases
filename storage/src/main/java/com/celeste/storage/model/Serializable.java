package com.celeste.storage.model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Serializable<T> extends java.io.Serializable {

  @NotNull
  T read(@NotNull final ResultSet result) throws SQLException, IOException;

  @NotNull
  Object[] write(@NotNull final T entity) throws SQLException, IOException;

}