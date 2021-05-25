package com.celeste.databases.messenger.model.database.dao.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.rabbitmq.RabbitMq;
import com.celeste.databases.messenger.model.database.pubsub.rabbitmq.RabbitMqPubSub;
import com.celeste.databases.messenger.model.entity.Listener;
import com.rabbitmq.client.Channel;

public final class RabbitMqDao extends AbstractMessengerDao<RabbitMq> {

  public RabbitMqDao(final RabbitMq messenger) {
    super(messenger);
  }

  @Override
  public void publish(final String channel, final String message)
      throws FailedConnectionException {
    try (final Channel connection = getDatabase().getChannel()) {
      connection.queueDeclare(channel, false, false, false, null);
      connection.basicPublish("", channel, false, null, message.getBytes());
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void subscribe(final String channel, final Listener listener)
      throws FailedConnectionException {
    try (final Channel connection = getDatabase().getChannel()) {
      final RabbitMqPubSub pubSub = new RabbitMqPubSub(listener);
      connection.queueDeclare(channel, false, false, false, null);
      connection.basicConsume(channel, true, pubSub, consumerTag -> {});
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
