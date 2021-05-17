package com.celeste.databases.messenger.model.database.dao;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.messenger.model.annotation.Listener;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import java.lang.reflect.Constructor;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
public abstract class AbstractMessengerDao<T extends Messenger> implements MessengerDao {

  private final T messenger;

  @Override
  public T getDatabase() {
    return messenger;
  }

  @Override
  public void subscribeAll(final Class<?> clazz, final Object instance)
      throws FailedConnectionException {
    subscribeAll(new SimpleImmutableEntry<>(clazz, instance));
  }

  @SafeVarargs
  @Override
  public final void subscribeAll(final Entry<Class<?>, Object>... entries)
      throws FailedConnectionException {
    try {
      final Class<?>[] parameters = Arrays.stream(entries)
          .map(Entry::getKey)
          .toArray(Class[]::new);

      final Object[] instances = Arrays.stream(entries)
          .map(Entry::getValue)
          .toArray();

      final Reflections reflections = new Reflections("");

      for (final Class<?> clazz : reflections.getTypesAnnotatedWith(Listener.class)) {
        final Listener annotation = Reflection.getAnnotation(clazz, Listener.class);
        final Constructor<?>[] constructors = Reflection.getConstructors(clazz);

        final Constructor<?> constructor = Arrays.stream(constructors)
            .filter(newConstructor -> Arrays.equals(newConstructor.getParameterTypes(), parameters))
            .findFirst()
            .orElse(null);

        if (constructor == null) {
          continue;
        }

        subscribe(annotation.value(), constructor.newInstance(instances));
      }
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getMessage(), exception.getCause());
    }
  }

}
