package com.celeste.databases.core.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validation {

  @SuppressWarnings("RedundantThrows")
  public static <T extends Throwable> boolean isTrue(final boolean expected,
      @NotNull final Supplier<T> orElse) throws T {
    if (!expected) {
      orElse.get();
    }

    return true;
  }

  @SuppressWarnings("RedundantThrows")
  @SneakyThrows(ReflectiveOperationException.class)
  public static <T extends Throwable> boolean isTrue(final boolean expected,
      @NotNull final Class<T> orElse) throws T {
    if (!expected) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The expression cannot be false");
    }

    return true;
  }

  public static boolean isTrue(final boolean expected) {
    if (!expected) {
      throw new IllegalArgumentException("The expression cannot be false");
    }

    return true;
  }

  @SuppressWarnings("RedundantThrows")
  public static <T extends Throwable> boolean isFalse(final boolean expected,
      @NotNull final Supplier<T> orElse) throws T {
    if (expected) {
      orElse.get();
    }

    return false;
  }

  @SuppressWarnings("RedundantThrows")
  @SneakyThrows(ReflectiveOperationException.class)
  public static <T extends Throwable> boolean isFalse(final boolean expected,
      @NotNull final Class<T> orElse) throws T {
    if (expected) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The expression cannot be true");
    }

    return false;
  }

  public static boolean isFalse(final boolean expected) {
    if (expected) {
      throw new IllegalArgumentException("The expression cannot be true");
    }

    return false;
  }

  @NotNull
  @SuppressWarnings({"ConstantConditions", "RedundantThrows"})
  public static <T, U extends Throwable> T notNull(@Nullable final T value,
      @NotNull final Supplier<U> orElse) throws U {
    if (value == null) {
      orElse.get();
    }

    return value;
  }

  @NotNull
  @SuppressWarnings({"ConstantConditions", "RedundantThrows"})
  @SneakyThrows(ReflectiveOperationException.class)
  public static <T, U extends Throwable> T notNull(@Nullable final T value,
      @NotNull final Class<U> orElse) throws U {
    if (value == null) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The object cannot be null");
    }

    return value;
  }

  @NotNull
  public static <T> T notNull(@Nullable final T value) {
    if (value == null) {
      throw new NullPointerException("The object cannot be null");
    }

    return value;
  }

  @NotNull
  @SuppressWarnings({"ConstantConditions", "RedundantThrows"})
  public static <T, U extends Throwable> T notEmpty(@Nullable final T value,
      @NotNull final Supplier<U> orElse) throws U {
    if (value == null
        || value instanceof Object[] && ((Object[]) value).length == 0
        || value instanceof Collection && ((Collection<?>) value).isEmpty()
        || value instanceof Map && ((Map<?, ?>) value).isEmpty()
    ) {
      orElse.get();
    }

    return value;
  }

  @NotNull
  @SuppressWarnings({"ConstantConditions", "RedundantThrows"})
  @SneakyThrows(ReflectiveOperationException.class)
  public static <T, U extends Throwable> T notEmpty(@Nullable final T value,
      @NotNull final Class<U> orElse) throws U {
    if (value == null) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The object cannot be null");
    }

    if (value instanceof String && ((String) value).length() == 0) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The String is empty");
    }

    if (value instanceof Object[] && ((Object[]) value).length == 0) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The array is empty");
    }

    if (value instanceof Collection && ((Collection<?>) value).size() == 0) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The collection is empty");
    }

    if (value instanceof Map && ((Map<?, ?>) value).size() == 0) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The map is empty");
    }

    return value;
  }

  @NotNull
  public static <T> T notEmpty(@Nullable final T value) {
    if (value == null) {
      throw new NullPointerException("The object cannot be null");
    }

    if (value instanceof String && ((String) value).length() == 0) {
      throw new IllegalArgumentException("The String is empty");
    }

    if (value instanceof Object[] && ((Object[]) value).length == 0) {
      throw new IllegalArgumentException("The array is empty");
    }

    if (value instanceof Collection && ((Collection<?>) value).size() == 0) {
      throw new IllegalArgumentException("The collection is empty");
    }

    if (value instanceof Map && ((Map<?, ?>) value).size() == 0) {
      throw new IllegalArgumentException("The map is empty");
    }

    return value;
  }

  @NotNull
  @SuppressWarnings({"ConstantConditions", "RedundantThrows"})
  public static <T, U extends Throwable> T noNullElements(@Nullable final T value,
      @NotNull final Supplier<U> orElse) throws U {
    if (value == null
        || (value instanceof Object[] && Arrays.stream(((Object[]) value))
        .anyMatch(Objects::isNull))
        || (value instanceof Collection && ((Collection<?>) value).stream()
        .anyMatch(Objects::isNull))
        || (value instanceof Map && ((Map<?, ?>) value).values().stream()
        .anyMatch(Objects::isNull))
    ) {
      orElse.get();
    }

    return value;
  }

  @NotNull
  @SuppressWarnings("ConstantConditions")
  @SneakyThrows(ReflectiveOperationException.class)
  public static <T> T noNullElements(@Nullable final T value, @NotNull final Class<T> orElse) {
    if (value == null) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The object cannot be null");
    }

    if (value instanceof Object[] && Arrays.stream((Object[]) value).anyMatch(Objects::isNull)) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The array cannot contain null elements");
    }

    if (value instanceof Collection && ((Collection<?>) value).stream().anyMatch(Objects::isNull)) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "he collection cannot contain null elements");
    }

    if (value instanceof Map && ((Map<?, ?>) value).values().stream().anyMatch(Objects::isNull)) {
      final Constructor<?> constructor = Reflection.getConstructor(orElse, String.class);
      Reflection.instance(constructor, "The map cannot contain null elements");
    }

    return value;
  }

  @NotNull
  public static <T> T noNullElements(@Nullable final T value) {
    if (value == null) {
      throw new NullPointerException("The object cannot be null");
    }

    if (value instanceof Object[] && Arrays.stream((Object[]) value).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("The array cannot contain null elements");
    }

    if (value instanceof Collection && ((Collection<?>) value).stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("The collection cannot contain null elements");
    }

    if (value instanceof Map && ((Map<?, ?>) value).values().stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("The map cannot contain null elements");
    }

    return value;
  }

}
