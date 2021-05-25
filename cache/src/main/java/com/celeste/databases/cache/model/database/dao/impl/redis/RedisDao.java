package com.celeste.databases.cache.model.database.dao.impl.redis;

import com.celeste.databases.cache.model.database.dao.AbstractCacheDao;
import com.celeste.databases.cache.model.database.provider.impl.redis.Redis;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
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
  public void delete(final String key) throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.del(key);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
