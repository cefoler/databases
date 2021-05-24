package com.celeste.databases.messenger.model.database.provider.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.dao.impl.rabbitmq.RabbitMqDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.rabbitmq.client.Channel;
import redis.clients.jedis.Jedis;

public interface RabbitMq extends Messenger {

  Channel getChannel() throws FailedConnectionException;

  @Override
  default MessengerDao createDao() {
    return new RabbitMqDao(this);
  }

}
