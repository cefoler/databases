package com.celeste.databases.storage.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.storage.model.database.dao.exception.DeleteException;
import com.celeste.databases.storage.model.database.dao.exception.FindException;
import com.celeste.databases.storage.model.database.dao.exception.SaveException;
import java.util.List;

public interface StorageDao<T> extends Dao {

  void save(final T... entities) throws SaveException;

  void delete(final T... entities) throws DeleteException;

  boolean contains(final Object key);

  T find(final Object key) throws FindException;

  List<T> findAll() throws FindException;

}
