package com.celeste.database.messenger.model.dao.rabbitmq;

import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.database.provider.rabbitmq.RabbitMQ;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class RabbitMQDAO implements MessengerDAO {

  private final RabbitMQ database;

  public RabbitMQDAO(@NotNull final RabbitMQ database) {
    this.database = database;
  }

  @Override
  public void publish(@NotNull final String message, @NotNull final String channelName) throws FailedConnectionException {
    try (final Channel channel = database.getConnection()) {
      channel.basicPublish("", channelName, false, null, message.getBytes());
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  @Override
  public void subscribe(@NotNull final Object instance, @NotNull final String channelName) throws FailedConnectionException {
    try (final Channel channel = database.getConnection()) {
      channel.queueDeclare(channelName, false, false, false, null);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

}