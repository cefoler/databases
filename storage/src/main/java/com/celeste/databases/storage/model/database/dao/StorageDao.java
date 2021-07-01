package com.celeste.databases.storage.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import java.util.Collection;
import java.util.List;

public interface StorageDao<T> extends Dao {

  @SuppressWarnings("unchecked")
  void save(final T... entities) throws FailedConnectionException;

  void save(final Collection<T> entities) throws FailedConnectionException;

  @SuppressWarnings("unchecked")
  void delete(final T... entities) throws FailedConnectionException;

  void delete(final Collection<T> entities) throws FailedConnectionException;

  boolean contains(final Object key) throws FailedConnectionException;

  T find(final Object key) throws ValueNotFoundException, FailedConnectionException;

  List<T> findAll() throws FailedConnectionException;

}
