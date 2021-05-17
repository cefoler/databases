package com.celeste.databases.storage.model.database.dao.impl.sql.type;

import com.celeste.databases.storage.model.entity.Entity;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum SqlType {

  SAVE("SAVE") {
    @Override
    public String getSql(final Entity<?> entity) {
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("REPLACE INTO ")
          .append(entity.getName())
          .append(" VALUES (");

      final int size = values.size();

      for (int index = 1; index <= size; index++) {
        final String expression = index != size ? "?," : "?);";

        builder.append(expression);
      }

      return builder.toString();
    }
  },
  DELETE("DELETE") {
    @Override
    public String getSql(final Entity<?> entity) {
      final String key = entity.getKey().getKey();

      final StringBuilder builder = new StringBuilder()
          .append("DELETE FROM ")
          .append(entity.getName())
          .append(" WHERE ")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  CONTAINS("CONTAINS") {
    @Override
    public String getSql(final Entity<?> entity) {
      final String key = entity.getKey().getKey();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT 1 FROM ")
          .append(entity.getName())
          .append(" WHERE ")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  FIND("FIND") {
    @Override
    public String getSql(final Entity<?> entity) {
      final String key = entity.getKey().getKey();
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT ");

      final Map<String, Field> cache = entity.getValues();

      for (final String name : values.keySet()) {
        final int size = cache.size();
        builder.append(name);

        if (size != 1) {
          builder.append(", ");
        }

        cache.remove(name);
      }

      builder.append(" FROM ")
          .append(entity.getName())
          .append(" WHERE ")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  FIND_ALL("FIND_ALL", "FINDALL") {
    @Override
    public String getSql(final Entity<?> entity) {
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT ");

      final Map<String, Field> cache = entity.getValues();

      for (final String name : values.keySet()) {
        builder.append(name);

        if (cache.size() != 1) {
          builder.append(", ");
        }

        cache.remove(name);
      }

      builder.append(" FROM ")
          .append(entity.getName())
          .append(";");

      return builder.toString();
    }
  },
  CREATE_TABLE("CREATE_TABLE", "CREATETABLE") {
    @Override
    public String getSql(final Entity<?> entity) {
      final Entry<String, Field> key = entity.getKey();
      final Map<String, Field> values = entity.getValues();

      final VariableType variableKey = VariableType.getVariable(key.getValue().getType());

      final StringBuilder builder = new StringBuilder()
          .append("CREATE TABLE IF NOT EXISTS ")
          .append(entity.getName())
          .append(" (")
          .append(key.getKey())
          .append(" ")
          .append(variableKey.getName())
          .append(" PRIMARY KEY, ");

      values.remove(key.getKey());

      final Map<String, Field> cache = new LinkedHashMap<>(values);

      for (final Entry<String, Field> entry : values.entrySet()) {
        final VariableType variable = VariableType.getVariable(entry.getValue().getType());

        builder.append(entry.getKey())
            .append(" ")
            .append(variable.getName());

        if (cache.size() != 1) {
          builder.append(", ");
        }

        cache.remove(entry.getKey());
      }

      builder.append(");");

      return builder.toString();
    }
  };

  private final List<String> names;

  SqlType(final String... names) {
    this.names = ImmutableList.copyOf(names);
  }

  public static SqlType getSql(final String sql) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(sql.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid sql: " + sql));
  }

  public static SqlType getSql(final String sql, @Nullable final SqlType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(sql.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

  public abstract String getSql(final Entity<?> entity);

}
