package com.celeste.databases.storage.model.database.dao.impl.sql;

import com.celeste.databases.core.adapter.exception.JsonSerializeException;
import com.celeste.databases.core.adapter.impl.jackson.JacksonAdapter;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
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

public final class SqlDao<T> extends AbstractStorageDao<Sql, T> {

  public SqlDao(final Sql storage, final Class<T> entity) {
    super(storage, entity);

    final String table = getEntity().getCollection();
    createTable(table);
  }

  @SafeVarargs
  @Override
  public final void save(final T... entities) {

  }

  @SafeVarargs
  @Override
  public final void delete(final T... entities) {

  }

  @Override
  public boolean contains(final Object key) {
    return false;
  }

  @Override
  public T find(final Object key) {
    return null;
  }

  @Override
  public List<T> findAll() {
    return null;
  }

  private void createTable(final String table) {

  }

  private int update(final String sql, final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    try (
        final Connection connection = getDatabase().getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      apply(statement, values);
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  private ResultSet query(final String sql, final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    try (
        final Connection connection = getDatabase().getConnection();
        final PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      apply(statement, values);
      return statement.executeQuery();
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  private void apply(final PreparedStatement statement, final Object... values)
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

  private T getFirst(final String sql, final Object... values) {

  }

  private List<T> getAll(final String sql, final Object... values) {

  }

  private Object[] serialize(final T entity) throws IllegalAccessException, JsonSerializeException {
    final Map<String, Object> values = getEntity().getValues(entity);
    final List<Object> newValues = new ArrayList<>();

    for (final Object object : values.values()) {
      final Object newObject = !instanceOfWrapper(object)
          ? JacksonAdapter.getInstance().serialize(object)
          : object;

      newValues.add(newObject);
    }

    return newValues.toArray();
  }

  private T deserialize(final ResultSet result) throws ReflectiveOperationException, SQLException {
    final Map<String, Field> values = getEntity().getValues();
    final T entity = getClazz().getConstructor().newInstance();

    final ResultSetMetaData metaData = result.getMetaData();
    final int columnCount = metaData.getColumnCount();

    final List<String> columnNames = new ArrayList<>();

    for (int index = 1; index <= columnCount; index++) {
      final String columnName = metaData.getColumnName(index);

      columnNames.add(columnName);
    }

    for (final String columnName : columnNames) {
      final Object object = result.getObject(columnName);
      final Field field = values.get(columnName);

      if (field == null) {
        continue;
      }

      final Class<?> clazz = field.getType();
      final Object newObject = getObject(object, clazz);

      field.set(entity, newObject);
      values.remove(columnName);
    }

    for (final Field field : values.values()) {
      field.set(entity, null);
    }

    return entity;
  }

  private boolean instanceOfWrapper(final Object object) {
    return object instanceof String || object instanceof Integer || object instanceof Double
        || object instanceof Boolean || object instanceof Long || object instanceof Float
        || object instanceof Character || object instanceof Byte || object instanceof Short;
  }

  private Object getObject(final Object object, final Class<?> clazz) {
    try {
      if (!(object instanceof String)) {
        return object;
      }

      final String json = object.toString();
      return JacksonAdapter.getInstance().deserialize(json, clazz);
    } catch (Exception exception) {
      return object;
    }
  }

}
