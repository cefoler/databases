package com.celeste.databases.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Wrapper {

  public static boolean isWrapper(final Object object) {
    return object instanceof String || object instanceof Integer || object instanceof Double
        || object instanceof Boolean || object instanceof Byte || object instanceof Short
        || object instanceof Long || object instanceof Float || object instanceof Character;
  }

  public static boolean isObject(final Object object, final Class<?> clazz) {
    return clazz.isInstance(object);
  }

  public static boolean isString(final Object object) {
    return object instanceof String;
  }

  public static boolean isInteger(final Object object) {
    return object instanceof Integer;
  }

  public static boolean isDouble(final Object object) {
    return object instanceof Double;
  }

  public static boolean isBoolean(final Object object) {
    return object instanceof Boolean;
  }

  public static boolean isByte(final Object object) {
    return object instanceof Byte;
  }

  public static boolean isShort(final Object object) {
    return object instanceof Short;
  }

  public static boolean isLong(final Object object) {
    return object instanceof Long;
  }

  public static boolean isFloat(final Object object) {
    return object instanceof Float;
  }

  public static boolean isCharacter(final Object object) {
    return object instanceof Character;
  }

  public static String toString(final Object object) {
    return String.valueOf(object);
  }

  public static Integer toInteger(final Object object) {
    final String convertedString = toString(object);
    return Integer.parseInt(convertedString);
  }

  public static Double toDouble(final Object object) {
    final String convertedString = toString(object);
    return Double.parseDouble(convertedString);
  }

  public static Boolean toBoolean(final Object object) {
    final String convertedString = toString(object);
    return Boolean.parseBoolean(convertedString);
  }

  public static Byte toByte(final Object object) {
    final String convertedString = toString(object);
    return Byte.parseByte(convertedString);
  }

  public static Short toShort(final Object object) {
    final String convertedString = toString(object);
    return Short.parseShort(convertedString);
  }

  public static Long toLong(final Object object) {
    final String convertedString = toString(object);
    return Long.parseLong(convertedString);
  }

  public static Float toFloat(final Object object) {
    final String convertedString = toString(object);
    return Float.parseFloat(convertedString);
  }

  public Character toCharacter(final Object object) {
    final String convertedString = toString(object);
    return convertedString.charAt(0);
  }

}
