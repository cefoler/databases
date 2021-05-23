package com.celeste.databases.storage.model.entity;

import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.core.util.Validation;
import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Name;
import com.celeste.databases.storage.model.annotation.Storable;
import com.celeste.databases.storage.model.annotation.Transient;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public final class Entity<T> {

  private final String name;

  private final Entry<String, Field> key;
  private final Map<String, Field> values;

  public Entity(final Class<T> clazz) {
    final Storable storable = Reflection.getAnnotation(clazz, Storable.class);

    Validation.notNull(storable, () ->
        new IllegalArgumentException("Entity doesn't have the @Storable annotation"));

    this.name = storable.value().toLowerCase();

    final Map<String, Field> fields = Arrays.stream(Reflection.getDcFields(clazz))
        .filter(field -> !Reflection.containsAnnotation(field, Transient.class))
        .collect(Collectors.toMap(
            this::getFieldName,
            field -> field,
            (field1, field2) -> field1,
            LinkedHashMap::new));

    this.key = fields.entrySet().stream()
        .filter(entry -> Reflection.containsAnnotation(entry.getValue(), Key.class))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Entity doesn't have the @Key annotation"));

    this.values = fields;
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
      newValues.put(entry.getKey(), Reflection.get(entry.getValue(), instance));
    }

    return newValues;
  }

}
