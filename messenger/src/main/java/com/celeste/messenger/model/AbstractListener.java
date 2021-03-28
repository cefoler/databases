package com.celeste.messenger.model;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

public abstract class AbstractListener extends JedisPubSub implements DeliverCallback {

  @Override
  public abstract void onMessage(@NotNull final String channel, @NotNull final String message);

  @Override
  public abstract void handle(@NotNull final String channel, @NotNull final Delivery delivery) throws IOException;

}