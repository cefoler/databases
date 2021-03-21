package com.celeste.database.provider.sql;

import com.celeste.database.provider.Database;
import com.celeste.database.provider.sql.function.SQLFunction;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface SQL extends Database {

    int executeUpdate(@NotNull final String sql, @NotNull final Object... values) throws SQLException;

    @NotNull
    ResultSet executeQuery(@NotNull final String sql, @NotNull final Object... values) throws SQLException;

    Connection getConnection() throws SQLException;

    default void applyValues(@NotNull final PreparedStatement statement, @NotNull final Object... values) throws SQLException {
        final AtomicInteger ai = new AtomicInteger(1);

        for (final Object value : values)
            statement.setObject(ai.getAndIncrement(), value);
    }

    default <T> T getFirst(@NotNull final String sql, @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values) throws SQLException {
        try (final ResultSet result = executeQuery(sql, values)) {
            return result.next() ? function.apply(result) : null;
        }
    }

    @NotNull
    default <T> List<T> getAll(@NotNull final String sql, @NotNull final SQLFunction<ResultSet, T> function, @NotNull final Object... values) throws SQLException {
        final List<T> arguments = new ArrayList<>();

        try (final ResultSet result = executeQuery(sql, values)) {
            while (result.next()) {
                final T argument = function.apply(result);
                if (argument != null) arguments.add(argument);
            }
        }

        return arguments;
    }

}
