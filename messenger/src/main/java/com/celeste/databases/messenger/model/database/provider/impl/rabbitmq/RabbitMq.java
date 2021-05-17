package com.celeste.databases.messenger.model.database.provider.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.dao.impl.rabbitmq.RabbitMqDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.rabbitmq.client.Channel;

public interface RabbitMq extends Messenger<Channel> {

  @Override
  default MessengerDao createDao() throws FailedConnectionException {
    return new RabbitMqDao(this);
  }

}
