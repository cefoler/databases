package com.celeste.database.storage.model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Storable<T> extends Serializable {

  @NotNull
  T read(@NotNull final ResultSet result) throws SQLException, IOException;

  @NotNull
  Object[] write(@NotNull final T value) throws SQLException, IOException;

}