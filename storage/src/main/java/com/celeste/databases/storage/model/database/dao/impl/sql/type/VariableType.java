package com.celeste.databases.storage.model.database.dao.impl.sql.type;

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

  CHARACTER(Character.class, "VARCHAR(16)"),
  CHARACTER_PRIMITIVE(char.class, "VARCHAR(16)"),
  STRING(String.class, "VARCHAR(10240)"),

  BYTE(Byte.class, "TINYINT"),
  BYTE_PRIMITIVE(byte.class, "TINYINT"),
  SHORT(Short.class, "SMALLINT"),
  SHORT_PRIMITIVE(short.class, "SMALLINT"),
  INTEGER(Integer.class, "INT"),
  INTEGER_PRIMITIVE(int.class, "INT"),
  LONG(Long.class, "BIGINT"),
  LONG_PRIMITIVE(long.class, "BIGINT"),

  FLOAT(Float.class, "FLOAT"),
  FLOAT_PRIMITIVE(float.class, "FLOAT"),
  DOUBLE(Double.class, "DOUBLE"),
  DOUBLE_PRIMITIVE(double.class, "DOUBLE"),

  BOOLEAN(Boolean.class, "BOOLEAN"),
  BOOLEAN_PRIMITIVE(boolean.class, "BOOLEAN"),

  DATE(java.util.Date.class, "TIMESTAMP"),
  DATESQL(java.sql.Date.class, "DATE"),
  TIME(Time.class, "TIME"),
  TIMESTAMP(Timestamp.class, "TIMESTAMP"),

  UUID(UUID.class, "VARCHAR(64)"),
  JSON(Void.class, "VARCHAR(10240)"),
  NULL(Void.class, "NULL");

  private final Class<?> clazz;
  private final String name;

  public static VariableType getVariable(final Class<?> clazz) {
    return Arrays.stream(values())
        .filter(type -> type.getClazz().equals(clazz) || type.getClazz().isInstance(clazz))
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
