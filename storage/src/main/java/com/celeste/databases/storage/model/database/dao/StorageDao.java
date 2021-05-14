package com.celeste.databases.storage.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import java.util.List;

public interface StorageDao<T> extends Dao {

  void save(final T... entities) throws FailedConnectionException;

  void delete(final T... entities) throws FailedConnectionException;

  boolean contains(final Object key) throws FailedConnectionException;

  T find(final Object key) throws ValueNotFoundException, FailedConnectionException;

  List<T> findAll() throws FailedConnectionException;

}
