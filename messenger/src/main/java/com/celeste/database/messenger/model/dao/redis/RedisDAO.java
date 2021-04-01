package com.celeste.database.messenger.model.dao.redis;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.database.provider.redis.Redis;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Getter
public class RedisDAO implements MessengerDAO {

  private final Redis database;

  public RedisDAO(@NotNull final Redis database) {
    this.database = database;
  }

  @Override
  public void publish(@NotNull final String message, @NotNull final String channelName) throws FailedConnectionException {
    try (final Jedis jedis = database.getConnection()) {
      jedis.publish(channelName, message);
    }
  }

  @Override
  public void subscribe(@NotNull final Object instance, @NotNull final String channelName) throws FailedConnectionException {
    try (final Jedis jedis = database.getConnection()) {
      new Thread(() -> jedis.subscribe((JedisPubSub) instance, channelName)).start();
    }
  }

}