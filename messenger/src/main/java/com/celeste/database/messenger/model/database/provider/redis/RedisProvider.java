package com.celeste.database.messenger.model.database.provider.redis;

import com.celeste.database.messenger.model.database.MessengerType;
import com.celeste.database.shared.exception.database.FailedConnectionException;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public final class RedisProvider implements Redis {

  @Getter(AccessLevel.PRIVATE)
  private final Properties properties;

  private JedisPool jedis;

  public RedisProvider(@NotNull final Properties properties)
      throws FailedConnectionException {
    this.properties = properties;

    init();
  }

  @Override
  public void init() throws FailedConnectionException {
    try {
      final JedisPoolConfig config = new JedisPoolConfig();

      config.setMinIdle(10);
      config.setMaxIdle(100);

      config.setMaxTotal(100);

      config.setMaxWaitMillis(30000);
      config.setMinEvictableIdleTimeMillis(600000);
      config.setNumTestsPerEvictionRun(-1);

      config.setTestOnBorrow(false);
      config.setTestOnReturn(false);
      config.setJmxEnabled(true);
      config.setTestWhileIdle(true);

      this.jedis = new JedisPool(config,
          properties.getProperty("hostname"),
          Integer.parseInt(properties.getProperty("port")),
          Protocol.DEFAULT_TIMEOUT,
          properties.getProperty("password"),
          false
      );
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  public void shutdown() {
    jedis.close();
  }

  @Override
  public boolean isClosed() {
    return jedis.isClosed();
  }

  @Override
  @NotNull
  public Jedis getConnection() throws FailedConnectionException {
    if (isClosed()) {
      throw new FailedConnectionException("Connection has been closed");
    }

    return jedis.getResource();
  }

  @Override
  @NotNull
  public MessengerType getType() {
    return MessengerType.REDIS;
  }

}