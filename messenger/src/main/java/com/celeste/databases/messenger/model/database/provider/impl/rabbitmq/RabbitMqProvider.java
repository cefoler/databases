package com.celeste.databases.messenger.model.database.provider.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import com.rabbitmq.client.Channel;

public final class RabbitMqProvider implements RabbitMq {

  @Override
  public void init() throws FailedConnectionException {

  }

  @Override
  public void shutdown() throws FailedShutdownException {

  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public Channel getConnection() throws FailedConnectionException {
    return null;
  }

  @Override
  public MessengerType getType() {
    return null;
  }
}
