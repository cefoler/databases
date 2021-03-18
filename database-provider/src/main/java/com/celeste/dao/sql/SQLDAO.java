package com.celeste.dao.sql;

import com.celeste.annotation.Query;
import com.celeste.dao.DAO;
import com.celeste.database.provider.sql.SQL;
import com.celeste.database.provider.sql.function.SQLFunction;
import com.celeste.exception.DAOException;
import com.celeste.exception.ValueNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
public final class SQLDAO<T> implements DAO<T> {

    @Getter
    private final SQL database;

    private final SQLFunction<ResultSet, T> read;
    private final SQLFunction<T, Object[]> write;

    @SuppressWarnings("unchecked")
    public SQLDAO(final SQL database, final Class<T> clazz) throws DAOException {
        try {
            this.database = database;

            final Method read = clazz.getMethod("read", ResultSet.class);
            final Method write = clazz.getMethod("write", clazz);

            final T instance = clazz.newInstance();

            this.read = result -> (T) read.invoke(instance, result);
            this.write = argument -> (Object[]) write.invoke(instance, argument);
        } catch (Throwable throwable) {
            throw new DAOException(throwable);
        }
    }

    @Override @SneakyThrows
    public void createTable() {
        final String sql = getAnnotation().value();
        database.executeUpdate(sql);
    }

    @Override @SafeVarargs @SneakyThrows
    public final void save(@NotNull final T... values) {
        final String sql = getAnnotation().value();

        for (final T value : values)
            database.executeUpdate(sql, write.apply(value));
    }

    @Override @SneakyThrows
    public void delete(final @NotNull Object key) {
        final String sql = getAnnotation().value();
        database.executeUpdate(sql, key);
    }

    @Override @SneakyThrows
    public boolean contains(final @NotNull Object key) {
        final String sql = getAnnotation().value();
        return database.executeQuery(sql, key).next();
    }

    @Override @NotNull @SneakyThrows(SQLException.class)
    public T find(final @NotNull Object key) throws ValueNotFoundException {
        final String sql = getAnnotation().value();

        final T argument = database.getFirst(sql, read, key);
        if (argument == null) throw new ValueNotFoundException("Value not found");

        return argument;
    }

    @Override @NotNull @SneakyThrows
    public List<T> findAll() {
        final String sql = getAnnotation().value();
        return database.getAll(sql, read);
    }

    @Nullable
    @SneakyThrows
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

        return null;
    }

}
