package com.celeste.databases.cache.model.database.dao.impl.redis;

import com.celeste.databases.cache.model.database.dao.AbstractCacheDao;
import com.celeste.databases.cache.model.database.provider.impl.redis.Redis;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import redis.clients.jedis.Jedis;

import java.util.Map;

public final class RedisDao extends AbstractCacheDao<Redis> {

  public RedisDao(final Redis cache) {
    super(cache);
  }

  @Override
  public String set(final String key, final String value) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.set(key, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public Long set(final String key, final String field, final String value) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hset(key, field, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String setExpiringAt(final String key, final int seconds, final String value) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.setex(key, seconds, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void delete(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.del(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void delete(final String key, final String field) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.hdel(key, field);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void delete(final String key, final String... fields) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.hdel(key, fields);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String get(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.get(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String get(String key, String value) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hget(key, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public Map<String, String> getAll(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hgetAll(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public boolean exists(final String... key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.exists(key) > 0;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public boolean exists(String key, String field) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hexists(key, field);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
