package com.celeste.database.messenger.model.dao;

import com.celeste.database.messenger.annotation.Subscribe;
import com.celeste.database.shared.model.dao.DAO;
import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public interface MessengerDAO extends DAO {

  void publish(@NotNull final String message, @NotNull final String channelName) throws FailedConnectionException;

  void subscribe(@NotNull final Object instance, @NotNull final String channelsName) throws FailedConnectionException;

  default void subscribeAll(@NotNull final Class<?> plugin, @NotNull final Object instance) throws FailedConnectionException, DAOException {
    try {
      for (final Class<?> clazz : new Reflections("").getTypesAnnotatedWith(Subscribe.class)) {
        final Subscribe annotation = clazz.getAnnotation(Subscribe.class);
        final Constructor<?> constructor = clazz.getConstructors()[0];

        final Object listener = constructor.getParameterCount() != 0
            ? Arrays.asList(constructor.getParameterTypes()).contains(plugin)
            ? constructor.newInstance(instance)
            : null
            : constructor.newInstance();

        if (listener == null) continue;

        subscribe(listener, annotation.value());
      }
    } catch (FailedConnectionException exception) {
      throw new FailedConnectionException(exception);
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }
  }

}
