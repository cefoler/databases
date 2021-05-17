package com.celeste.databases.cache.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;

public interface CacheDao extends Dao {

  String set(final String key, final String value) throws FailedConnectionException;

  void delete(final String key) throws FailedConnectionException;

}
