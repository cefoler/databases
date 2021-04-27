package com.celeste.database.messenger.model;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import redis.clients.jedis.JedisPubSub;

/**
 * The AbstractListener is the class that should be extended in all classes that are a PubSub
 *
 * <p>This class contains 2 methods:</p>
 * <p>onMessage - Method that is going to be called when the PubSub
 * receives a message</p>
 */
public abstract class AbstractListener extends JedisPubSub implements DeliverCallback {

  @Override
  public abstract void onMessage(final String channel, final String message);

  @Override
  public abstract void handle(final String channel, final Delivery delivery) throws IOException;

}