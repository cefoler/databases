package com.celeste.databases.messenger.model.database.pubsub.rabbitmq;

import com.celeste.databases.messenger.model.entity.Listener;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RabbitMqPubSub implements DeliverCallback {

  private final Listener listener;

  @Override
  public void handle(final String channel, final Delivery delivery) {
    final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
    listener.receive(channel, message);
  }

}
