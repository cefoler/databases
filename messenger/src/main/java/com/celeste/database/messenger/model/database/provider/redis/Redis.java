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

  /**
   * Returns the JedisCluster, if the ConnectionType
   * is LOCAL, this object is null.
   * @return JedisCluster
   */
  @Nullable
  JedisCluster getCluster();

  /**
   * Returns the Jedis connection from that slot in the Cluster.
   * If the ConnectionType is LOCAL, this object is null.
   * @param slot int
   *
   * @return Jedis
   */
  @Nullable
  Jedis getConnectionFromSlot(int slot);

  /**
   * Creates a new RedisDAO
   * @return MessengerDAO
   */
  @Override @NotNull
  default MessengerDAO createDAO() {
    return new RedisDAO(this);
  }

}