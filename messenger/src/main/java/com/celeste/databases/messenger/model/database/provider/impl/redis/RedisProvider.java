package com.celeste.databases.messenger.model.database.provider.impl.redis;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public final class RedisProvider implements Redis {

  private final RemoteCredentials credentials;
  private JedisPool jedis;

  public RedisProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      Class.forName("redis.clients.jedis.JedisPool");

      final JedisPoolConfig config = new JedisPoolConfig();

      config.setMinIdle(1);
      config.setMaxIdle(20);

      config.setMaxTotal(20);

      config.setMaxWaitMillis(30000);
      config.setMinEvictableIdleTimeMillis(600000);
      config.setNumTestsPerEvictionRun(-1);

      config.setTestOnBorrow(false);
      config.setTestOnReturn(false);
      config.setJmxEnabled(true);
      config.setTestWhileIdle(true);

      this.jedis = new JedisPool(config, credentials.getHostname(), credentials.getPort(),
          Protocol.DEFAULT_TIMEOUT, credentials.getPassword(), credentials.isSsl());
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      jedis.close();
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    try {
      return jedis.isClosed();
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public Jedis getJedis() throws FailedConnectionException {
    try {
      return jedis.getResource();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public MessengerType getMessengerType() {
    return MessengerType.REDIS;
  }

}
