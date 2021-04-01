package com.celeste.database.messenger.model.dao.rabbitmq;

import com.celeste.database.messenger.annotation.Subscribe;
import com.celeste.database.messenger.model.AbstractListener;
import com.celeste.database.messenger.model.dao.MessengerDAO;
import com.celeste.database.messenger.model.database.provider.rabbitmq.RabbitMQ;
import com.celeste.messenger.model.*;
import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

@Getter
public class RabbitMQDAO implements MessengerDAO {

  private final RabbitMQ database;

  @SneakyThrows({ IllegalAccessException.class, InstantiationException.class })
  public RabbitMQDAO(@NotNull final RabbitMQ database) throws DAOException, FailedConnectionException {
    try {
      this.database = database;
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }

    for (final Class<? extends AbstractListener> clazz : new Reflections("").getSubTypesOf(AbstractListener.class)) {
      final Subscribe annotation = clazz.getAnnotation(Subscribe.class);

      if (annotation == null) continue;

      subscribe(clazz.newInstance(), annotation.value());
    }
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