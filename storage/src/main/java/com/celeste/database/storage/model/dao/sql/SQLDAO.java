package com.celeste.database.storage.model.dao.sql;

import com.celeste.database.shared.exceptions.dao.DAOException;
import com.celeste.database.shared.exceptions.dao.ValueNotFoundException;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.storage.annotations.Query;
import com.celeste.database.storage.model.dao.StorageDAO;
import com.celeste.database.storage.model.database.provider.sql.SQL;
import com.celeste.database.storage.model.database.provider.sql.function.SQLFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation for the StorageDAO interface.
 *
 * <p>The SQLDAO executes the methods of the interfaces
 * with the SQL driver</p>
 *
 * @param <T> Object
 */
@Getter(AccessLevel.PRIVATE)
public final class SQLDAO<T> implements StorageDAO<T> {

  @Getter
  private final SQL database;

  private final SQLFunction<ResultSet, T> read;
  private final SQLFunction<T, Object[]> write;

  @SuppressWarnings("unchecked")
  public SQLDAO(@NotNull final SQL database, @NotNull final Class<T> clazz) throws DAOException {
    try {
      this.database = database;

      final Method read = clazz.getMethod("read", ResultSet.class);
      final Method write = clazz.getMethod("write");

      final T instance = clazz.getConstructor().newInstance();

      this.read = result -> (T) read.invoke(instance, result);
      this.write = argument -> (Object[]) write.invoke(instance);
    } catch (Throwable throwable) {
      throw new DAOException(throwable);
    }
  }

  /**
   * Creates the table in the SQL database
   * The parameter name is null, this was only added for the MongoDB.
   */
  @Override @SneakyThrows
  public void createTable(final String name) {
    final String sql = getAnnotation().value();
    database.executeUpdate(sql);
  }

  /**
   * Saves the value into the SQL database
   * @param key This is null, please ignore.
   * @param value T
   */
  @Override @SneakyThrows
  public final void save(final Object key, @NotNull final T value) {
    final String sql = getAnnotation().value();
    database.executeUpdate(sql, write.apply(value));
  }

  /**
   * Deletes a value from the database through
   * the key.
   * @param key Object
   */
  @Override @SneakyThrows
  public void delete(@NotNull final Object key) {
    final String sql = getAnnotation().value();
    database.executeUpdate(sql, key);
  }

  /**
   * Check if it contains a value through the
   * key provided
   * @param key Object
   *
   * @return Boolean
   */
  @Override @SneakyThrows
  public boolean contains(@NotNull final Object key) {
    final String sql = getAnnotation().value();

    try (final ResultSet result = database.executeQuery(sql, key)) {
      return result.next();
    }
  }

  /**
   * Finds the object through the key provided
   * @param key Object
   *
   * @return T
   * @throws ValueNotFoundException Throws if no value exists on that key
   */
  @Override @NotNull @SneakyThrows(FailedConnectionException.class)
  public T find(@NotNull final Object key) throws ValueNotFoundException {
    final String sql = getAnnotation().value();

    final T argument = database.getFirst(sql, read, key);

    if (argument == null) throw new ValueNotFoundException("Value not found");

    return argument;
  }

  /**
   * Gets all values storaged in the database
   * @return List
   */
  @Override @NotNull @SneakyThrows
  public List<T> findAll() {
    final String sql = getAnnotation().value();
    return database.getAll(sql, read);
  }

  @NotNull @SneakyThrows
  public Query getAnnotation() {
    final StackTraceElement[] elements = Thread.currentThread().getStackTrace();

    for (int i = 2; i < elements.length; i++) {
      final StackTraceElement element = elements[i];

      final Class<?> clazz = Class.forName(element.getClassName());
      final String methodName = element.getMethodName().startsWith("lambda$") ?
          element.getMethodName().split("\\$")[1] :
          element.getMethodName();

      final Method method = Arrays.stream(clazz.getMethods())
          .filter(m -> m.getName().equals(methodName))
          .findFirst()
          .orElse(null);

      if (method == null) continue;

      final Query annotation = method.getAnnotation(Query.class);

      if (annotation == null) continue;

      return annotation;
    }

    throw new RuntimeException("Query annotation not found");
  }

}