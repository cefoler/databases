package com.celeste.databases.messenger.model.database.dao.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.AbstractMessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.rabbitmq.RabbitMq;
import com.rabbitmq.client.Channel;

public final class RabbitMqDao extends AbstractMessengerDao<RabbitMq> {

  public RabbitMqDao(final RabbitMq messenger) {
    super(messenger);
  }

  @Override
  public void publish(final String channelName, final String message)
      throws FailedConnectionException {
    try (final Channel connection = getDatabase().getChannel()) {
      connection.basicPublish("", channelName, false, null, message.getBytes());
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public void subscribe(final String[] channels, final Object instance)
      throws FailedConnectionException {
    try (final Channel connection = getDatabase().getChannel()) {
      connection.queueDeclare(channels[0], false, false, false, null);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
