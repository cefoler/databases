package com.celeste.database.messenger.model.database.provider.redis;

import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.shared.model.type.ConnectionType;
import lombok.AccessLevel;
import lombok.Getter;
import com.celeste.database.messenger.model.database.MessengerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.*;

import java.util.Properties;

public final class RedisProvider implements Redis {

  @Getter(AccessLevel.PRIVATE)
  private final Properties properties;
  private final ConnectionType connectionType;

  private JedisPool jedis;

  /**
   * Instance of the JedisCluster. If the connection
   * type is LOCAL, this will be null.
   */
  @Nullable
  private JedisCluster cluster;

  public RedisProvider(@NotNull final Properties properties, final ConnectionType connectionType) throws FailedConnectionException {
    this.properties = properties;
    this.connectionType = connectionType;

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

      switch (connectionType) {
        case LOCAL: {
          this.jedis = new JedisPool(
              config,
              properties.getProperty("hostname"),
              Integer.parseInt(properties.getProperty("port")),
              Protocol.DEFAULT_TIMEOUT,
              properties.getProperty("password"),
              false
          );
        }
        case CLUSTER: {
          // TODO: Add multiple hosts and ports
          final HostAndPort hostAndPort = new HostAndPort(
              properties.getProperty("hostname"),
              Integer.getInteger(properties.getProperty("port"))
          );

          this.cluster = new JedisCluster(
              hostAndPort,
              config
          );
        }
      }
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override @Nullable
  public JedisCluster getCluster() {
    return cluster;
  }

  @Override
  public void shutdown() {
    jedis.close();
  }

  @Override
  public boolean isClosed() {
    return jedis.isClosed();
  }

  @Override @NotNull
  public MessengerType getType() {
    return MessengerType.REDIS;
  }

  @Override
  public @NotNull ConnectionType getConnectionType() {
    return connectionType;
  }

  @Override @NotNull
  public Jedis getConnection() throws FailedConnectionException {
    if (isClosed()) throw new FailedConnectionException("Connection has been closed");

    return jedis.getResource();
  }

  /**
   * Returns a Jedis connection with that slot in the Cluster
   * @param slot Slot of the Jedis in the Cluster
   *
   * @return Jedis
   */
  @Override @Nullable
  public Jedis getConnectionFromSlot(final int slot) {
    if (connectionType == ConnectionType.LOCAL) throw new UnsupportedOperationException("You can't use Cluster methods in a local connection");

    return cluster != null ? cluster.getConnectionFromSlot(slot) : null;
  }

}