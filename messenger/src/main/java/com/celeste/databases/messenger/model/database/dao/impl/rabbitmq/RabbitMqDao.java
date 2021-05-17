package com.celeste.databases.messenger.model.database.dao.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.rabbitmq.RabbitMq;

public final class RabbitMqDao implements MessengerDao {

  private final RabbitMq messenger;

  public RabbitMqDao(final RabbitMq messenger) {
    this.messenger = messenger;
  }

  @Override
  public Database getDatabase() {
    return messenger;
  }

}
