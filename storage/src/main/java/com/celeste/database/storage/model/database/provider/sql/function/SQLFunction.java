package com.celeste.database.storage.model.database.provider.sql.function;

import java.sql.SQLException;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SQLFunction<T, U> extends Function<T, U> {

  @Nullable
  U applyThrowing(@NotNull final T input) throws SQLException, ReflectiveOperationException;

  @Override
  @Nullable
  @SneakyThrows
  default U apply(@NotNull final T input) {
    return applyThrowing(input);
  }

}