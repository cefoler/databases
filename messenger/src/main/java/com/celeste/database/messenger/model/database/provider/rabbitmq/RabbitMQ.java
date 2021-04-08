package com.celeste.database.messenger.model.database.provider.rabbitmq;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
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