package com.celeste.database.messenger.model.database.provider.redis;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.dao.redis.RedisDAO;
import com.celeste.database.messenger.model.database.provider.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public interface Redis extends Messenger<Jedis> {

  /**
   * Creates a new RedisDAO
   *
   * @return MessengerDAO
   */
  @Override
  @NotNull
  default MessengerDAO createDAO() {
    return new RedisDAO(this);
  }

}