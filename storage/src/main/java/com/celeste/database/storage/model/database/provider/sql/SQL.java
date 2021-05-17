package com.celeste.database.storage.model.database.provider.sql;

import com.celeste.database.shared.exception.dao.DAOException;
import com.celeste.database.shared.exception.dao.ValueNotFoundException;
import com.celeste.database.shared.exception.database.FailedConnectionException;
import com.celeste.database.storage.model.Storable;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.dao.sql.SQLDAO;
import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.database.provider.sql.function.SQLFunction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SQL extends Storage {

  /**
   * Executes a update in the database through that Query
   *
   * @param sql    String
   * @param values Object...
   * @return Integer, it should return 1 if it was successful
   * @throws ValueNotFoundException    Throws if the value wasn't found in the database
   * @throws FailedConnectionException Throws if the connection failed
   */
  int executeUpdate(@NotNull final String sql, @NotNull final Object... values)
      throws ValueNotFoundException, FailedConnectionException;

  /**
   * Executes a Query by the String provided and uses the values for the specified Query values
   * needed.
   *
   * @param sql    String
   * @param values Object...
   * @return ResultSet
   * @throws ValueNotFoundException    Throws if the value wasn't found
   * @throws FailedConnectionException Throws if the connection failed
   */
  @NotNull
  ResultSet executeQuery(@NotNull final String sql, @NotNull final Object... values)
      throws ValueNotFoundException, FailedConnectionException;

  /**
   * Gets the connection from the database
   *
   * @return Connection
   * @throws FailedConnectionException Throws if the connection failed
   */
  @NotNull
  Connection getConnection() throws FailedConnectionException;

  /**
   * Creates a DAO for that Object. The object should explicitly implement Storable to enable
   * serializing
   *
   * @param entity Class
   * @param <T>    Entity
   * @return StorageDAO
   * @throws DAOException Throws if it wasn't able to create a DAO
   */
  @Override
  @NotNull
  default <T extends Storable<T>> StorageDAO<T> createDAO(@NotNull final Class<T> entity)
      throws DAOException {
    return new SQLDAO<>(this, entity);
  }

  default void applyValues(@NotNull final PreparedStatement statement,
      @NotNull final Object... values) throws ValueNotFoundException {
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
  default <T> T getFirst(@NotNull final String sql,
      @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    try (final ResultSet result = executeQuery(sql, values)) {
      return result.next() ? function.apply(result) : null;
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }
  }

  @NotNull
  default <T> List<T> getAll(@NotNull final String sql,
      @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values)
      throws ValueNotFoundException, FailedConnectionException {
    final List<T> arguments = new ArrayList<>();

    try (final ResultSet result = executeQuery(sql, values)) {
      while (result.next()) {
        final T argument = function.apply(result);
        if (argument != null) {
          arguments.add(argument);
        }
      }
    } catch (SQLException exception) {
      throw new ValueNotFoundException(exception);
    }

    return arguments;
  }

}