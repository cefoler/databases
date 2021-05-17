package com.celeste.database.messenger.model.dao;

import com.celeste.database.messenger.annotation.Subscribe;
import com.celeste.database.shared.exception.dao.DAOException;
import com.celeste.database.shared.exception.database.FailedConnectionException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

/**
 * This class represents the universal DAO for the Messengers, here you can publish a message into
 * the channel and subscribe instances.
 */
public interface MessengerDAO {

  /**
   * Publish a new message through the channel provided
   *
   * @param message     String
   * @param channelName String
   * @throws FailedConnectionException Throws when the connection fails
   */
  void publish(@NotNull final String message, @NotNull final String channelName)
      throws FailedConnectionException;

  /**
   * Subscribes a new PubSub and it's channel name
   *
   * @param instance     Object
   * @param channelsName String
   * @throws FailedConnectionException Throws when the connection fails
   */
  void subscribe(@NotNull final Object instance, @NotNull final String channelsName)
      throws FailedConnectionException;

  default void subscribeAll(@NotNull final Class<?> plugin, @NotNull final Object instance)
      throws FailedConnectionException, DAOException {
    try {
      for (final Class<?> clazz : new Reflections("").getTypesAnnotatedWith(Subscribe.class)) {
        final Subscribe annotation = clazz.getAnnotation(Subscribe.class);
        final Constructor<?> constructor = clazz.getConstructors()[0];

        final Object listener = constructor.getParameterCount() != 0
            ? Arrays.asList(constructor.getParameterTypes()).contains(plugin)
            ? constructor.newInstance(instance)
            : null
            : constructor.newInstance();

        if (listener == null) {
          continue;
        }

        subscribe(listener, annotation.value());
      }
    } catch (FailedConnectionException exception) {
      throw new FailedConnectionException(exception);
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }
  }

}
