package com.celeste.databases.storage.model.database.dao.impl.sql.type;

import com.celeste.databases.core.adapter.impl.jackson.JacksonAdapter;
import com.celeste.databases.core.util.Wrapper;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public enum VariableType {

  CHARACTER(Character.class, "TINYTEXT"),
  STRING(String.class, "TEXT"),

  BYTE(Byte.class, "TINYINT"),
  SHORT(Short.class, "SMALLINT"),
  INTEGER(Integer.class, "INT"),
  LONG(Long.class, "BIGINT"),

  FLOAT(Float.class, "FLOAT"),
  DOUBLE(Double.class, "DOUBLE"),

  BOOLEAN(Boolean.class, "BOOL"),

  DATE(java.util.Date.class, "TIMESTAMP"),
  DATESQL(java.sql.Date.class, "DATE"),
  TIME(Time.class, "TIME"),
  TIMESTAMP(Timestamp.class, "TIMESTAMP"),

  UUID(UUID.class, "CHAR(36)"),
  JSON(Void.class, "TEXT"),
  NULL(Void.class, "NULL");

  private final Class<?> clazz;
  private final String name;

  public static VariableType getVariable(final Class<?> clazz) {
    return Arrays.stream(values())
        .filter(type -> type.getClazz().equals(clazz))
        .findFirst()
        .orElse(JSON);
  }

  public static VariableType getVariable(@Nullable final Object object) {
    return Arrays.stream(values())
        .filter(type -> type.getClazz().isInstance(object))
        .findFirst()
        .orElseGet(() -> object != null ? JSON : NULL);
  }

}
