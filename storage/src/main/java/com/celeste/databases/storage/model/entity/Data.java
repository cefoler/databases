package com.celeste.databases.storage.model.entity;

import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.core.util.Validation;
import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Name;
import com.celeste.databases.storage.model.annotation.Storable;
import com.celeste.databases.storage.model.annotation.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public final class Data<T> {

  private final String name;

  private final Entry<String, Field> key;
  private final Map<String, Field> values;

  public Data(final Class<T> clazz) {
    final Storable storable = Reflection.getAnnotation(clazz, Storable.class);

    Validation.notNull(storable, () ->
        new IllegalArgumentException("Data doesn't have the @Storable annotation"));

    this.name = storable.value().toLowerCase();
    final Field[] fields = Reflection.getDcFields(clazz);

    final LinkedList<Field> converted = new LinkedList<>();

    Arrays.stream(fields)
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .collect(Collectors.toCollection(LinkedList::new))
        .descendingIterator()
        .forEachRemaining(converted::addFirst);

    Class<?> superClazz = clazz.getSuperclass();

    while (superClazz != null) {
      final Field[] superFields = Reflection.getDcFields(superClazz);

      Arrays.stream(superFields)
          .filter(field -> !Modifier.isStatic(field.getModifiers()))
          .collect(Collectors.toCollection(LinkedList::new))
          .descendingIterator()
          .forEachRemaining(converted::addFirst);

      superClazz = superClazz.getSuperclass();
    }

    this.values = converted.stream()
        .sorted((field1, field2) -> Reflection.containsAnnotation(field1, Key.class)
            ? -1
            : Reflection.containsAnnotation(field2, Key.class) ? 1 : 0)
        .filter(field -> !Reflection.containsAnnotation(field, Transient.class)
            && !Modifier.isTransient(field.getModifiers()))
        .collect(Collectors.toMap(this::getFieldName, field -> field, (field1, field2) -> field1,
            LinkedHashMap::new));

    this.key = values.entrySet().stream()
        .filter(entry -> Reflection.containsAnnotation(entry.getValue(), Key.class))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Data doesn't have the @Key annotation"));
  }

  private String getFieldName(final Field field) {
    return Reflection.containsAnnotation(field, Name.class)
        ? Reflection.getAnnotation(field, Name.class).value().toLowerCase()
        : field.getName().toLowerCase();
  }

  public String getName() {
    return name;
  }

  public Entry<String, Field> getKey() {
    return key;
  }

  @SneakyThrows
  public Object getKey(final T instance) {
    return Reflection.get(key.getValue(), instance);
  }

  public Map<String, Field> getValues() {
    return new LinkedHashMap<>(values);
  }

  @SneakyThrows
  public Map<String, Object> getValues(final T instance) {
    final Map<String, Object> newValues = new LinkedHashMap<>();

    for (final Entry<String, Field> entry : values.entrySet()) {
      final Object object = Reflection.get(entry.getValue(), instance);
      newValues.put(entry.getKey(), object);
    }

    return newValues;
  }

}
