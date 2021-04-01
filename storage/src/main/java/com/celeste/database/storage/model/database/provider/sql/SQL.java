package com.celeste.database.storage.model.database.provider.sql;

import com.celeste.database.shared.model.dao.exception.DAOException;
import com.celeste.database.shared.model.dao.exception.ValueNotFoundException;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.dao.sql.SQLDAO;
import com.celeste.database.storage.model.database.provider.sql.function.SQLFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface SQL extends Storage {

  int executeUpdate(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException;

  @NotNull
  ResultSet executeQuery(@NotNull final String sql, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException;

  @NotNull
  Connection getConnection() throws FailedConnectionException;

  @Override @NotNull
  default <T extends Storable> StorageDAO<T> createDAO(@NotNull final Class<T> entity) throws DAOException {
    return new SQLDAO<>(this, entity);
  }

  default void applyValues(@NotNull final PreparedStatement statement, @NotNull final Object... values) throws ValueNotFoundException {
    final AtomicInteger integer = new AtomicInteger(1);

    for (final Object value : values) {
      try {
        statement.setObject(integer.getAndIncrement(), value);
      } catch (SQLException exception) {
        throw new ValueNotFoundException(exception);
      }
    }
  }

  @Nullable
  default <T> T getFirst(@NotNull final String sql, @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    try (final ResultSet result = executeQuery(sql, values)) {
      return result.next() ? function.apply(result) : null;
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @NotNull
  default <T> List<T> getAll(@NotNull final String sql, @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values) throws ValueNotFoundException, FailedConnectionException {
    final List<T> arguments = new ArrayList<>();

    try (final ResultSet result = executeQuery(sql, values)) {
      while (result.next()) {
        final T argument = function.apply(result);
        if (argument != null) arguments.add(argument);
      }
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }

    return arguments;
  }

}