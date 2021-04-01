package com.celeste.database.messenger.model.database.provider.rabbitmq;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.rabbitmq.client.Channel;
import com.celeste.database.messenger.model.dao.rabbitmq.RabbitMQDAO;
import com.celeste.database.messenger.model.database.provider.Messenger;
import org.jetbrains.annotations.NotNull;

public interface RabbitMQ extends Messenger<Channel> {

  @Override @NotNull
  default MessengerDAO createDAO() throws DAOException, FailedConnectionException {
    return new RabbitMQDAO(this);
  }

}