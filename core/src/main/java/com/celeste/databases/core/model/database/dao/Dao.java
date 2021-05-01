package com.celeste.databases.core.model.database.dao;

import com.celeste.databases.core.model.database.provider.Database;
import org.jetbrains.annotations.NotNull;

public interface Dao {

  @NotNull
  Database getDatabase();

}
