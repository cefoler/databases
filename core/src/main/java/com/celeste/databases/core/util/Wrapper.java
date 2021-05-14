package com.celeste.databases.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Wrapper {

  public static boolean isWrapper(final Object object) {
    return object instanceof String || object instanceof Integer || object instanceof Double
        || object instanceof Boolean || object instanceof Long || object instanceof Float
        || object instanceof Character || object instanceof Byte || object instanceof Short;
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

  public static boolean isLong(final Object object) {
    return object instanceof Long;
  }

  public static boolean isFloat(final Object object) {
    return object instanceof Float;
  }

  public static boolean isCharacter(final Object object) {
    return object instanceof Character;
  }

  public static boolean isByte(final Object object) {
    return object instanceof Byte;
  }

  public static boolean isShort(final Object object) {
    return object instanceof Short;
  }

  public static String convertToString(final Object object) {
    return String.valueOf(object);
  }

  public static Integer convertToInteger(final Object object) {
    final String convertedString = convertToString(object);
    return Integer.parseInt(convertedString);
  }

  public static Double convertToDouble(final Object object) {
    final String convertedString = convertToString(object);
    return Double.parseDouble(convertedString);
  }

  public static Boolean convertToBoolean(final Object object) {
    final String convertedString = convertToString(object);
    return Boolean.parseBoolean(convertedString);
  }

  public static Long convertToLong(final Object object) {
    final String convertedString = convertToString(object);
    return Long.parseLong(convertedString);
  }

  public static Float convertToFloat(final Object object) {
    final String convertedString = convertToString(object);
    return Float.parseFloat(convertedString);
  }

  public Character convertToCharacter(final Object object) {
    final String convertedString = convertToString(object);
    return convertedString.charAt(0);
  }

  public static Byte convertToByte(final Object object) {
    final String convertedString = convertToString(object);
    return Byte.parseByte(convertedString);
  }

  public static Short convertToShort(final Object object) {
    final String convertedString = convertToString(object);
    return Short.parseShort(convertedString);
  }

}
