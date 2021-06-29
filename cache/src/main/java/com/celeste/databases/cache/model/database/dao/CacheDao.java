package com.celeste.databases.cache.model.database.dao;

import com.celeste.databases.core.model.database.dao.Dao;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import java.util.Map;

public interface CacheDao extends Dao {

  String set(final String key, final String value) throws FailedConnectionException;

  Long set(final String key, final String field, final String value)
      throws FailedConnectionException;

  String expire(final String key, final String value, final long seconds)
      throws FailedConnectionException;

  Long expire(final String key, final long seconds)
      throws FailedConnectionException;

  void increment(final String key, final String value, final double amount) throws FailedConnectionException;

  void delete(final String key) throws FailedConnectionException;

  void delete(final String key, final String field) throws FailedConnectionException;

  void delete(final String key, final String... fields) throws FailedConnectionException;

  boolean contains(final String... keys) throws FailedConnectionException;

  boolean contains(final String key, final String field) throws FailedConnectionException;

  String find(final String key) throws FailedConnectionException;

  String find(final String key, final String value) throws FailedConnectionException;

  Map<String, String> findAll(final String key) throws FailedConnectionException;

}
