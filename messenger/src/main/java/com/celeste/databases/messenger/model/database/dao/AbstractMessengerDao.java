package com.celeste.databases.messenger.model.database.dao;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.messenger.model.annotation.Subscribe;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.celeste.databases.messenger.model.entity.Listener;
import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
public abstract class AbstractMessengerDao<T extends Messenger> implements MessengerDao {

  protected final T messenger;

  @Override
  public T getDatabase() {
    return messenger;
  }

  @Override
  public void subscribeAll(final String prefix, final Class<?> clazz, final Object instance)
      throws FailedConnectionException {
    subscribeAll(prefix, Maps.immutableEntry(clazz, instance));
  }

  @SafeVarargs
  @Override
  public final void subscribeAll(final String prefix, final Entry<Class<?>, Object>... entries)
      throws FailedConnectionException {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections(prefix);

      for (final Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)
          .stream()
          .filter(clazz -> clazz.isAnnotationPresent(Subscribe.class))
          .collect(Collectors.toSet())) {
        final Subscribe annotation = Reflection.getAnnotation(clazz, Subscribe.class);
        final Constructor<? extends Listener>[] constructors = Reflection.getConstructors(clazz);

        final Constructor<? extends Listener> constructor = Arrays.stream(constructors)
            .filter(newConstructor -> Arrays.equals(newConstructor.getParameterTypes(), parameters))
            .findFirst()
            .orElse(null);

        if (constructor == null) {
          continue;
        }

        final Listener listener = constructor.newInstance(instances);

        for (final String channel : annotation.value()) {
          subscribe(channel, listener);
        }
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

}
