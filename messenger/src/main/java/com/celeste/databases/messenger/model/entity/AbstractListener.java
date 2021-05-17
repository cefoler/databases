package com.celeste.databases.messenger.model.entity;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.nio.charset.StandardCharsets;
import redis.clients.jedis.JedisPubSub;

public abstract class AbstractListener extends JedisPubSub implements DeliverCallback, Listener {

  @Override
  public void onMessage(final String channel, final String message) {
    receive(channel, message);
  }

  @Override
  public void handle(final String channel, final Delivery delivery) {
    receive(channel, new String(delivery.getBody(), StandardCharsets.UTF_8));
  }

}
