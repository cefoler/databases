package com.celeste.databases.messenger.model.database.dao.impl.redis;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.redis.Redis;
import com.celeste.databases.messenger.model.database.pubsub.redis.RedisPubSub;
import com.celeste.databases.messenger.model.entity.Listener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public final class RedisDao extends AbstractMessengerDao<Redis> {

  public RedisDao(final Redis messenger) {
    super(messenger);
  }

  @Override
  public void publish(final String channel, final String message)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      jedis.publish(channel, message);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void subscribe(final String channel, final Listener listener)
      throws FailedConnectionException {
    try (final Jedis jedis = getDatabase().getJedis()) {
      final RedisPubSub pubSub = new RedisPubSub(listener);
      jedis.subscribe(pubSub, channel);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
