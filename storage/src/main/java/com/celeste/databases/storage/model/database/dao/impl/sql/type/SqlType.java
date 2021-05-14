package com.celeste.databases.storage.model.database.dao.impl.sql.type;

import com.celeste.databases.storage.model.entity.Entity;
import java.lang.reflect.Field;
import java.util.Map;

public enum SqlType {

  SAVE {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("REPLACE INTO ")
          .append(table)
          .append(" VALUES (");

      final int size = values.size();

      for (int index = 1; index <= size; index++) {
        final String expression = index == size ? "?," : "?);";

        builder.append(expression);
      }

      return builder.toString();
    }
  },
  DELETE {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final String key = entity.getKeyName();

      final StringBuilder builder = new StringBuilder()
          .append("DELETE FROM ")
          .append(table)
          .append(" WHERE ")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  CONTAINS {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final String key = entity.getKeyName();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT 1 FROM ")
          .append(table)
          .append(" WHERE")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  FIND {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final String key = entity.getKeyName();
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT ");

      for (final String name : values.keySet()) {
        final int size = values.size();
        builder.append(name);

        if (size != 1) {
          builder.append(", ");
        }

        values.remove(name);
      }

      builder.append(" FROM ")
          .append(table)
          .append(" WHERE ")
          .append(key)
          .append(" = ?;");

      return builder.toString();
    }
  },
  FIND_ALL {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final Map<String, Field> values = entity.getValues();

      final StringBuilder builder = new StringBuilder()
          .append("SELECT ");

      for (final String name : values.keySet()) {
        final int size = values.size();
        builder.append(name);

        if (size != 1) {
          builder.append(", ");
        }

        values.remove(name);
      }

      builder.append(" FROM ")
          .append(table)
          .append(";");

      return builder.toString();
    }
  },
  CREATE_TABLE {
    @Override
    public String getSql(final Entity<?> entity) {
      final String table = entity.getName();
      final Field key = entity.getKey();
      final String keyName = entity.getKeyName();
      final Map<String, Field> values = entity.getValues();

      final Class<?> keyClazz = key.getType();
      final VariableType keyVariable = VariableType.getVariable(keyClazz);
      final String keyVariableName = keyVariable.getName();

      final StringBuffer buffer = new StringBuffer(" ")
          .append("CREATE TABLE IF NOT EXISTS")
          .append(table)
          .append("(")
          .append(keyName)
          .append(keyVariableName)
          .append(",");

      values.forEach((name, field) -> {
        final Class<?> clazz = field.getType();
        final VariableType variable = VariableType.getVariable(clazz);
        final String variableName = variable.getName();

        buffer.append(name)
            .append(variableName)
            .append(",");
      });

      buffer.append(");");

      return buffer.toString();
    }
  };

  public abstract String getSql(final Entity<?> entity);

}
