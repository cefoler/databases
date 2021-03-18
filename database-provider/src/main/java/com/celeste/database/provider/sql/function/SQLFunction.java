package com.celeste.database.provider.sql.function;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.function.Function;

public interface SQLFunction<T, U> extends Function<T, U> {

    U applyThrowing(@NotNull final T input) throws SQLException, ReflectiveOperationException;

    @Override @SneakyThrows
    default U apply(@NotNull final T input) {
        return applyThrowing(input);
    }

}
