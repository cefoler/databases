package com.celeste.databases.messenger.model.database.pubsub.redis;

import com.celeste.databases.messenger.model.entity.Listener;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.JedisPubSub;

@RequiredArgsConstructor
public final class RedisPubSub extends JedisPubSub {

  private final Listener listener;

  @Override
  public void onMessage(final String channel, final String message) {
    listener.receive(channel, message);
  }

}
