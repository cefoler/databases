package com.celeste.databases.cache.model.database.dao.impl.redis;

import com.celeste.databases.cache.model.database.dao.AbstractCacheDao;
import com.celeste.databases.cache.model.database.provider.impl.redis.Redis;
import com.google.common.annotations.Beta;

@Beta
public final class RedisDao extends AbstractCacheDao<Redis> {

  public RedisDao(final Redis cache) {
    super(cache);
  }

}
