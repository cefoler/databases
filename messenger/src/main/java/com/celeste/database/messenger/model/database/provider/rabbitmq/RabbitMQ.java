package com.celeste.database.messenger.model.database.provider.rabbitmq;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.rabbitmq.client.Channel;
import com.celeste.database.messenger.model.dao.rabbitmq.RabbitMQDAO;
import com.celeste.database.messenger.model.database.provider.Messenger;
import org.jetbrains.annotations.NotNull;

public interface RabbitMQ extends Messenger<Channel> {

  /**
   * Creates a new RabbitDAO
   * @return MessengerDAO
   */
  @Override @NotNull
  default MessengerDAO createDAO() {
    return new RabbitMQDAO(this);
  }

}