package com.celeste.databases.cache.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;

import java.util.Map;

public interface CacheDao extends Dao {

  String set(final String key, final String value) throws FailedConnectionException;

  Long set(final String key, final String field, final String value) throws FailedConnectionException;

  String setExpiring(final String key, final int seconds, final String value) throws FailedConnectionException;

  void delete(final String key) throws FailedConnectionException;

  void delete(final String key, final String field) throws FailedConnectionException;

  void delete(final String key, final String... fields) throws FailedConnectionException;

  Map<String, String> getAll(final String key) throws FailedConnectionException;

  boolean exists(final String... keys) throws FailedConnectionException;

}
