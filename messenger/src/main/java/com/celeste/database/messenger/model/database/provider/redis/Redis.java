package com.celeste.database.messenger.model.database.provider.redis;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.dao.redis.RedisDAO;
import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.messenger.model.database.provider.Messenger;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

public interface Redis extends Messenger<Jedis> {

  @Override @NotNull
  default MessengerDAO createDAO() throws DAOException, FailedConnectionException {
    return new RedisDAO(this);
  }

}