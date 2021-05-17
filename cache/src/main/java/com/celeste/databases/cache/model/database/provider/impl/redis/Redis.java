package com.celeste.databases.cache.model.database.provider.impl.redis;

import com.celeste.databases.cache.model.database.dao.CacheDao;
import com.celeste.databases.cache.model.database.dao.impl.redis.RedisDao;
import com.celeste.databases.cache.model.database.provider.Cache;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import redis.clients.jedis.Jedis;

public interface Redis extends Cache {

  Jedis getJedis() throws FailedConnectionException;

  @Override
  default CacheDao createDao() {
    return new RedisDao(this);
  }

}
