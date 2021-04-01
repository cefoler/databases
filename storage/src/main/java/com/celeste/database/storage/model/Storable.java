package com.celeste.database.storage.model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Storable extends Serializable {

  @NotNull
  Object read(@NotNull final ResultSet result) throws SQLException, IOException;

  @NotNull
  Object[] write() throws SQLException, IOException;

}