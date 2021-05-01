package com.celeste.databases.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reflection {

  @NotNull
  public static Class<?> getClazz(@NotNull final String path) throws ClassNotFoundException {
    return Class.forName(path);
  }

  @NotNull
  public static Class<?> getClazz(@NotNull final Field field) {
    return field.getType();
  }

  @NotNull
  public static Class<?>[] getClasses(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getClasses();
  }

  @NotNull
  public static Class<?> getClasses(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getClasses()[size];
  }

  @NotNull
  public static Class<?>[] getClasses(@NotNull final Class<?> clazz) {
    return clazz.getClasses();
  }

  @NotNull
  public static Class<?> getClasses(@NotNull final Class<?> clazz, final int size) {
    return clazz.getClasses()[size];
  }

  @NotNull
  public static Class<?>[] getDcClasses(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses();
  }

  @NotNull
  public static Class<?> getDcClasses(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses()[size];
  }

  @NotNull
  public static Class<?>[] getDcClasses(@NotNull final Class<?> clazz) {
    return clazz.getDeclaredClasses();
  }

  @NotNull
  public static Class<?> getDcClasses(@NotNull final Class<?> clazz, final int size) {
    return clazz.getDeclaredClasses()[size];
  }

  @NotNull
  public static Constructor<?> getConstructor(@NotNull final String path,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getConstructor(parameterClass);
  }

  @NotNull
  public static Constructor<?> getConstructor(@NotNull final Class<?> clazz,
      @NotNull final Class<?>... parameterClass) throws NoSuchMethodException {
    return clazz.getConstructor(parameterClass);
  }

  @NotNull
  public static Constructor<?> getDcConstructor(@NotNull final String path,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  @NotNull
  public static Constructor<?> getDcConstructor(@NotNull final Class<?> clazz,
      @NotNull final Class<?>... parameterClass) throws NoSuchMethodException {
    final Constructor<?> constructor = clazz.getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  @NotNull
  public static Constructor<?>[] getConstructors(@NotNull final String path)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors();
  }

  @NotNull
  public static Constructor<?> getConstructors(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors()[size];
  }

  @NotNull
  public static Constructor<?>[] getConstructors(@NotNull final Class<?> clazz) {
    return clazz.getConstructors();
  }

  @NotNull
  public static Constructor<?> getConstructors(@NotNull final Class<?> clazz, final int size) {
    return clazz.getConstructors()[size];
  }

  @NotNull
  public static Constructor<?>[] getDcConstructors(@NotNull final String path)
      throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  @NotNull
  public static Constructor<?> getDcConstructors(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  @NotNull
  public static Constructor<?>[] getDcConstructors(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  @NotNull
  public static Constructor<?> getDcConstructors(@NotNull final Class<?> clazz, final int size) {
    final Constructor<?> constructor = clazz.getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  @NotNull
  public static Method getMethod(@NotNull final String path, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getMethod(methodName, parameterClass);
  }

  @NotNull
  public static Method getMethod(@NotNull final Class<?> clazz, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws NoSuchMethodException {
    return clazz.getMethod(methodName, parameterClass);
  }

  @NotNull
  public static Method getDcMethod(@NotNull final String path, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    final Method method = getClazz(path).getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  @NotNull
  public static Method getDcMethod(@NotNull final Class<?> clazz, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass) throws NoSuchMethodException {
    final Method method = clazz.getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  @NotNull
  public static Method[] getMethods(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getMethods();
  }

  @NotNull
  public static Method getMethods(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getMethods()[size];
  }

  @NotNull
  public static Method[] getMethods(@NotNull final Class<?> clazz) {
    return clazz.getMethods();
  }

  @NotNull
  public static Method getMethods(@NotNull final Class<?> clazz, final int size) {
    return clazz.getMethods()[size];
  }

  @NotNull
  public static Method[] getDcMethods(@NotNull final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  @NotNull
  public static Method getDcMethods(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Method method = getClazz(path).getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  @NotNull
  public static Method[] getDcMethods(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  @NotNull
  public static Method getDcMethods(@NotNull final Class<?> clazz, final int size) {
    final Method method = clazz.getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  @NotNull
  public static Field getField(@NotNull final String path, @NotNull final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    return getClazz(path).getField(variableName);
  }

  @NotNull
  public static Field getField(@NotNull final Class<?> clazz, @NotNull final String variableName)
      throws NoSuchFieldException {
    return clazz.getField(variableName);
  }

  @NotNull
  public static Field getDcField(@NotNull final String path, @NotNull final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    final Field field = getClazz(path).getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  @NotNull
  public static Field getDcField(@NotNull final Class<?> clazz, @NotNull final String variableName)
      throws NoSuchFieldException {
    final Field field = clazz.getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  @NotNull
  public static Field[] getFields(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getFields();
  }

  @NotNull
  public static Field getFields(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getFields()[size];
  }

  @NotNull
  public static Field[] getFields(@NotNull final Class<?> clazz) {
    return clazz.getFields();
  }

  @NotNull
  public static Field getFields(@NotNull final Class<?> clazz, final int size) {
    return clazz.getFields()[size];
  }

  @NotNull
  public static Field[] getDcFields(@NotNull final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  @NotNull
  public static Field getDcFields(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Field field = getClazz(path).getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  @NotNull
  public static Field[] getDcFields(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  @NotNull
  public static Field getDcFields(@NotNull final Class<?> clazz, final int size) {
    final Field field = clazz.getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  @NotNull
  public static Object instance(@NotNull final Constructor<?> constructor)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance();
  }

  @NotNull
  public static Object instance(@NotNull final Constructor<?> constructor,
      @NotNull final Object... args)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(args);
  }

  @NotNull
  public static Object invoke(@NotNull final Method method, @NotNull final Object instance,
      @NotNull final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(instance, args);
  }

  @NotNull
  public static Object invokeStatic(@NotNull final Method method, @NotNull final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(null, args);
  }

  @NotNull
  public static Object get(@NotNull final Field field, @NotNull final Object instance)
      throws IllegalAccessException {
    return field.get(instance);
  }

  @NotNull
  public static Object getStatic(@NotNull final Field field) throws IllegalAccessException {
    return field.get(null);
  }

}
