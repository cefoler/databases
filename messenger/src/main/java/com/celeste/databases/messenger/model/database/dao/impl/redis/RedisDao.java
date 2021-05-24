package com.celeste.databases.messenger.model.database.dao.impl.redis;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.redis.Redis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public final class RedisDao extends AbstractMessengerDao<Redis> {

  public RedisDao(final Redis messenger) {
    super(messenger);
  }

  @Override
  public void publish(final String channelName, final String message)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.publish(channelName, message);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getMessage(), exception.getCause());
    }
  }

  @Override
  public void subscribe(final String channelName, final Object instance)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      new Thread(() -> jedis.subscribe((JedisPubSub) instance, channelName)).start();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getMessage(), exception.getCause());
    }
  }

}
