package com.celeste.database.messenger.model.database.provider.redis;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.dao.redis.RedisDAO;
import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.messenger.model.database.provider.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public interface Redis extends Messenger<Jedis> {

  @Nullable
  JedisCluster getCluster();

  Jedis getConnectionFromSlot(int slot);

  @Override @NotNull
  default MessengerDAO createDAO() throws DAOException, FailedConnectionException {
    return new RedisDAO(this);
  }

}