package com.celeste.databases.storage.model.database.dao.impl.sql;

import com.celeste.databases.core.adapter.impl.jackson.JacksonAdapter;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.util.Validation;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
import com.celeste.databases.storage.model.database.dao.impl.sql.type.SqlType;
import com.celeste.databases.storage.model.database.dao.impl.sql.type.VariableType;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

public final class SqlDao<T> extends AbstractStorageDao<Sql, T> {

  private final String save;
  private final String delete;

  private final String contains;

  private final String find;
  private final String findAll;

  private final String createTable;

  public SqlDao(final Sql storage, final Class<T> entity) throws FailedConnectionException {
    super(storage, entity);

    this.save = SqlType.SAVE.getSql(getEntity());
    this.delete = SqlType.DELETE.getSql(getEntity());

    this.contains = SqlType.CONTAINS.getSql(getEntity());

    this.find = SqlType.FIND.getSql(getEntity());
    this.findAll = SqlType.FIND_ALL.getSql(getEntity());

    this.createTable = SqlType.CREATE_TABLE.getSql(getEntity());

    createTable();
  }

  @SafeVarargs
  @Override
  public final void save(final T... entities) throws FailedConnectionException {
    for (final T entity : entities) {
      executeUpdate(save, serialize(entity));
    }
  }

  @SafeVarargs
  @Override
  public final void delete(final T... entities) throws FailedConnectionException {
    for (final T entity : entities) {
      executeUpdate(delete, serialize(entity));
    }
  }

  @Override
  public boolean contains(final Object key) throws FailedConnectionException {
    try (final ResultSet result = executeQuery(contains, serializeObject(key))) {
      return result.next();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception.getCause());
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
    try (
        final Connection connection = getDatabase().getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception.getCause());
    }
  }

  private ResultSet executeQuery(final String sql, final Object... values)
      throws FailedConnectionException {
    try (
        final Connection connection = getDatabase().getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      applyValues(statement, values);
      return statement.executeQuery();
    } catch (SQLException exception) {
      throw new FailedConnectionException(exception.getCause());
    }
  }

  private void applyValues(final PreparedStatement statement, final Object... values)
      throws FailedConnectionException {
    final AtomicInteger index = new AtomicInteger(1);

    for (final Object value : values) {
      try {
        statement.setObject(index.getAndIncrement(), value);
      } catch (Exception exception) {
        throw new FailedConnectionException(exception.getCause());
      }
    }
  }

  private T getFirst(final String sql, final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    try (final ResultSet result = executeQuery(sql, values)) {
      Validation.isFalse(result.next(), () -> new ValueNotFoundException("Value not found"));
      return deserialize(result);
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception.getCause());
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
      throw new FailedConnectionException(exception.getCause());
    }

    return entities;
  }

  @SneakyThrows
  private Object[] serialize(final T entity) {
    final Map<String, Object> values = getEntity().getValues(entity);

    return values.values().stream()
        .map(this::serializeObject)
        .toArray();
  }

  @SneakyThrows(ReflectiveOperationException.class)
  private T deserialize(final ResultSet result) throws SQLException {
    final Map<String, Field> values = getEntity().getValues();
    final T entity = getClazz().getConstructor().newInstance();

    final ResultSetMetaData metaData = result.getMetaData();
    final int columnCount = metaData.getColumnCount();

    final List<String> columnNames = new ArrayList<>();

    for (int index = 1; index <= columnCount; index++) {
      columnNames.add(metaData.getColumnName(index));
    }

    for (final String columnName : columnNames) {
      final Object object = result.getObject(columnName);
      final Field field = values.get(columnName);

      if (field == null) {
        continue;
      }

      field.set(entity, deserializeObject(object, field.getType()));
      values.remove(columnName);
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
  @SneakyThrows
  private Object deserializeObject(@Nullable final Object object, final Class<?> clazz) {
    final VariableType variable = VariableType.getVariable(object);

    return variable.equals(VariableType.JSON)
        ? JacksonAdapter.getInstance().deserialize(String.valueOf(object), clazz)
        : object;
  }

}
