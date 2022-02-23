package com.celeste.databases.storage.model.database.dao.impl.sql;

import com.celeste.databases.core.adapter.exception.JsonDeserializeException;
import com.celeste.databases.core.adapter.impl.jackson.JacksonAdapter;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.util.Reflection;
import com.celeste.databases.core.util.Validation;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
import com.celeste.databases.storage.model.database.dao.impl.sql.type.SqlType;
import com.celeste.databases.storage.model.database.dao.impl.sql.type.VariableType;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import com.celeste.databases.storage.model.database.type.StorageType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public final class SqlDao<T> extends AbstractStorageDao<Sql, T> {

  private final String save;
  private final String delete;

  private final String contains;

  private final String find;
  private final String findAll;

  private final String createTable;

  public SqlDao(final Sql storage, final Class<T> clazz) throws FailedConnectionException {
    super(storage, clazz);

    this.save = storage.getStorageType().equals(StorageType.H2)
        ? SqlType.SAVE.getSql(data).replace("REPLACE", "MERGE")
        : SqlType.SAVE.getSql(data);

    this.delete = SqlType.DELETE.getSql(data);

    this.contains = SqlType.CONTAINS.getSql(data);

    this.find = SqlType.FIND.getSql(data);
    this.findAll = SqlType.FIND_ALL.getSql(data);

    this.createTable = SqlType.CREATE_TABLE.getSql(data);

    createTable();
  }

  @SafeVarargs
  @Override
  public final void save(final T... entities) throws FailedConnectionException {
    save(Arrays.asList(entities));
  }

  @Override
  public void save(final Collection<T> entities) throws FailedConnectionException {
    for (final T entity : entities) {
      executeUpdate(save, serialize(entity));
    }
  }

  @SafeVarargs
  @Override
  public final void delete(final T... entities) throws FailedConnectionException {
    for (final T entity : entities) {
      final Object key = data.getKey(entity);
      executeUpdate(delete, serializeObject(key));
    }
  }

  @Override
  public void delete(final Collection<T> entities) throws FailedConnectionException {
    for (final T entity : entities) {
      delete(entity);
    }
  }

  @Override
  public boolean contains(final Object key) throws FailedConnectionException {
    try (final ResultSet result = executeQuery(contains, serializeObject(key))) {
      return result.next();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public T find(final Object key) throws ValueNotFoundException, FailedConnectionException {
    return getFirst(find, serializeObject(key));
  }

  @Override
  public List<T> findAll() throws FailedConnectionException {
    return getAll(findAll);
  }

  private void createTable() throws FailedConnectionException {
    executeUpdate(createTable);
  }

  private int executeUpdate(final String sql, final Object... values)
      throws FailedConnectionException {
    final Sql database = getDatabase();
    final StorageType type = database.getStorageType();

    try (
        final Connection connection = storage.getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }
  }

  private ResultSet executeQuery(final String sql, final Object... values)
      throws FailedConnectionException {
    final Sql database = getDatabase();
    final StorageType type = database.getStorageType();

    try (
        final Connection connection = storage.getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeQuery();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }
  }

  private void applyValues(final PreparedStatement statement, final Object... values)
      throws FailedConnectionException {
    final AtomicInteger index = new AtomicInteger(1);

    for (final Object value : values) {
      try {
        statement.setObject(index.getAndIncrement(), value);
      } catch (Exception exception) {
        throw new FailedConnectionException(exception);
      }
    }
  }

  private T getFirst(final String sql, final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    try (final ResultSet result = executeQuery(sql, values)) {
      Validation.isFalse(result.next(), () -> new ValueNotFoundException("Value not found."));
      return deserialize(result);
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  private List<T> getAll(final String sql, final Object... values)
      throws FailedConnectionException {
    final List<T> entities = new ArrayList<>();

    try (final ResultSet result = executeQuery(sql, values)) {
      while (result.next()) {
        entities.add(deserialize(result));
      }
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception);
    }

    return entities;
  }

  @SneakyThrows
  private Object[] serialize(final T entity) {
    final Map<String, Object> values = data.getValues(entity);

    return values.values().stream()
        .map(this::serializeObject)
        .toArray();
  }

  @SneakyThrows(ReflectiveOperationException.class)
  private T deserialize(final ResultSet result) throws SQLException {
    final Map<String, Field> values = data.getValues();
    final T entity = Reflection.getDcConstructor(clazz).newInstance();

    final ResultSetMetaData metaData = result.getMetaData();
    final int count = metaData.getColumnCount();

    final List<String> names = new ArrayList<>();

    for (int index = 1; index <= count; index++) {
      names.add(metaData.getColumnName(index));
    }

    for (final String name : names) {
      final Field field = values.get(name);

      if (field == null) {
        continue;
      }

      final Object object = result.getObject(name);
      final Object valueDeserialized = deserializeObject(object, field);

      field.set(entity, valueDeserialized);
      values.remove(name);
    }

    for (final Field field : values.values()) {
      field.set(entity, null);
    }

    return entity;
  }

  @Nullable
  @SneakyThrows
  private Object serializeObject(@Nullable final Object object) {
    final VariableType variable = VariableType.getVariable(object);

    return variable.equals(VariableType.JSON) || variable.equals(VariableType.UUID)
        ? JacksonAdapter.getInstance().serialize(object)
        : object;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private Object deserializeObject(@Nullable final Object object, final Field field) {
    final Class<?> clazz = field.getType();
    final VariableType variable = VariableType.getVariable(object);

    if (variable == VariableType.STRING) {
      try {
        final String replaced = field.getGenericType().getTypeName()
            .replaceAll(".*<|>.*", "")
            .replace("[]", "");

        if (clazz.equals(Map.class)) {
          final String converted = String.valueOf(object);
          final Map<?, ?> deserialized = JacksonAdapter.getInstance().deserialize(converted,
              LinkedHashMap.class);

          final List<? extends Class<?>> classes = Arrays.stream(replaced.split(", "))
              .map(path -> {
                try {
                  return Class.forName(path);
                } catch (ClassNotFoundException ignored) {
                  throw new NullPointerException("Class not found");
                }
              })
              .collect(Collectors.toList());

          final Class<?> keyClazz = classes.get(0);
          final Class<?> valueClazz = classes.get(1);

          return deserialized.entrySet().stream()
              .map(entry -> {
                try {
                  final Object key = entry.getKey();
                  final Object value = entry.getValue();

                  final Object keyDeserialized = JacksonAdapter.getInstance()
                      .deserialize(String.valueOf(key), keyClazz);
                  final Object valueDeserialized = JacksonAdapter.getInstance()
                      .deserialize(String.valueOf(value), valueClazz);

                  return new SimpleEntry<>(keyClazz, valueDeserialized);
                } catch (JsonDeserializeException ignored) {
                  return entry;
                }
              })
              .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (value1, value2) -> value1,
                  LinkedHashMap::new));
        }

        final Class<?> type = Class.forName(replaced);

        if (clazz.equals(Set.class)) {
          return JacksonAdapter.getInstance()
              .deserialize(String.valueOf(object), LinkedHashSet.class).stream()
              .map(subObject -> {
                try {
                  return JacksonAdapter.getInstance().deserialize(String.valueOf(subObject), type);
                } catch (JsonDeserializeException ignored) {
                  return subObject;
                }
              })
              .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        if (clazz.equals(List.class)) {
          return JacksonAdapter.getInstance().deserialize(String.valueOf(object),
                  ArrayList.class).stream()
              .map(subObject -> {
                try {
                  return JacksonAdapter.getInstance().deserialize(String.valueOf(subObject), type);
                } catch (JsonDeserializeException ignored) {
                  return subObject;
                }
              })
              .collect(Collectors.toList());
        }

        return JacksonAdapter.getInstance().deserialize(String.valueOf(object), clazz);
      } catch (JsonDeserializeException | ClassNotFoundException exception) {
        return object;
      }
    }

    return object;
  }

}
