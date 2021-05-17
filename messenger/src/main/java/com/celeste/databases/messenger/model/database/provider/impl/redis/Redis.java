package com.celeste.databases.messenger.model.database.provider.impl.redis;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.dao.impl.redis.RedisDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import redis.clients.jedis.Jedis;

public interface Redis extends Messenger {

  Jedis getJedis() throws FailedConnectionException;

  @Override
  default MessengerDao createDao() throws FailedConnectionException {
    return new RedisDao(this);
  }

}
