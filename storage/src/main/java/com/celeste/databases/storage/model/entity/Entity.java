package com.celeste.databases.storage.model.entity;

import com.celeste.databases.core.util.Validation;
import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Name;
import com.celeste.databases.storage.model.annotation.Storable;
import com.celeste.databases.storage.model.annotation.Transient;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public final class Entity<T> {

  private final String collection;

  private final Field key;
  private final Map<String, Field> values;

  public Entity(final Class<T> clazz) {
    final Storable storable = clazz.getAnnotation(Storable.class);

    Validation.notNull(storable, () ->
        new IllegalArgumentException("An @Storable annotation was not found on that entity."));

    this.collection = storable.value().toLowerCase();

    final List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .filter(field -> field.getAnnotation(Transient.class) != null)
        .collect(Collectors.toList());

    this.key = fields.stream()
        .filter(field -> field.getAnnotation(Key.class) != null)
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("An @Key annotation was not found on that entity."));

    this.values = fields.stream()
        .collect(Collectors.toMap(
            field -> field.getAnnotation(Name.class) != null
                ? field.getAnnotation(Name.class).value().toLowerCase()
                : field.getName().toLowerCase(),
            field -> field,
            (field, field2) -> field2,
            LinkedHashMap::new));
  }

  public String getCollection() {
    return collection;
  }

  public Field getKey() {
    return key;
  }

  public Object getKey(final T instance) throws IllegalAccessException {
    return key.get(instance);
  }

  public Map<String, Field> getValues() {
    return new LinkedHashMap<>(values);
  }

  public Map<String, Object> getValues(final T instance) throws IllegalAccessException {
    final Map<String, Object> newValues = new LinkedHashMap<>();

    for (final Entry<String, Field> entry : values.entrySet()) {
      final String key = entry.getKey();
      final Object value = entry.getValue().get(instance);

      newValues.put(key, value);
    }

    return newValues;
  }

}
