package com.celeste.databases.cache.model.database.dao.impl.redis;

import com.celeste.databases.cache.model.database.dao.AbstractCacheDao;
import com.celeste.databases.cache.model.database.provider.impl.redis.Redis;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import java.util.Map;
import redis.clients.jedis.Jedis;

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
  public Long set(final String key, final String field, final String value)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hset(key, field, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String expire(final String key, final String value, final long seconds)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.setex(key, seconds, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public Long expire(final String key, final long seconds)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.expire(key, seconds);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void increment(final String key, final String value, final double amount) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.hincrByFloat(key, value, amount);
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
  public boolean contains(final String... key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.exists(key) > 0;
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public boolean contains(String key, String field) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hexists(key, field);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String find(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.get(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public String find(String key, String value) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hget(key, value);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public Map<String, String> findAll(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      return jedis.hgetAll(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
