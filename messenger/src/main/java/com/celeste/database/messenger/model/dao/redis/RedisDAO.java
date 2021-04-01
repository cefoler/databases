package com.celeste.database.messenger.model.dao.redis;

import com.celeste.database.messenger.annotation.Subscribe;
import com.celeste.database.messenger.model.AbstractListener;
import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.database.provider.redis.Redis;
import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Getter
public class RedisDAO implements MessengerDAO {

  private final Redis database;

  @SneakyThrows({ IllegalAccessException.class, InstantiationException.class })
  public RedisDAO(@NotNull final Redis database) throws DAOException, FailedConnectionException {
    try {
      this.database = database;
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }

    for (final Class<? extends AbstractListener> clazz : new Reflections("").getSubTypesOf(AbstractListener.class)) {
      final Subscribe annotation = clazz.getAnnotation(Subscribe.class);

      if (annotation == null) continue;

      subscribe(clazz.newInstance(), annotation.value());
    }
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