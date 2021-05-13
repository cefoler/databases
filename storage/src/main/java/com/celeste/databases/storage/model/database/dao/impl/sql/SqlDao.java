package com.celeste.databases.storage.model.database.dao.impl.sql;

import com.celeste.databases.core.model.database.dao.exception.DaoException;
import com.celeste.databases.core.model.database.dao.exception.ValueNotFoundException;
import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.storage.model.database.dao.AbstractStorageDao;
import com.celeste.databases.storage.model.database.provider.impl.sql.Sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    final AtomicInteger integer = new AtomicInteger(1);

    for (final Object value : values) {
      try {
        statement.setObject(integer.getAndIncrement(), value);
      } catch (Exception exception) {
        throw new FailedConnectionException(exception);
      }
    }
  }

  private T getFirst(final String sql, final Object... values) {
  }

  private List<T> getAll(final String sql, final Object... values) {

  }

}
