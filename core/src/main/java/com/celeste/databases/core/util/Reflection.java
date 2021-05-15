package com.celeste.databases.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reflection {

  public static Class<?> getClazz(final String path) throws ClassNotFoundException {
    return Class.forName(path);
  }

  public static Class<?> getClazz(final Field field) {
    return field.getType();
  }

  public static Class<?>[] getClasses(final String path) throws ClassNotFoundException {
    return getClazz(path).getClasses();
  }

  public static Class<?> getClasses(final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getClasses()[size];
  }

  public static Class<?>[] getClasses(final Class<?> clazz) {
    return clazz.getClasses();
  }

  public static Class<?> getClasses(final Class<?> clazz, final int size) {
    return clazz.getClasses()[size];
  }

  public static Class<?>[] getDcClasses(final String path) throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses();
  }

  public static Class<?> getDcClasses(final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses()[size];
  }

  public static Class<?>[] getDcClasses(final Class<?> clazz) {
    return clazz.getDeclaredClasses();
  }

  public static Class<?> getDcClasses(final Class<?> clazz, final int size) {
    return clazz.getDeclaredClasses()[size];
  }

  public static Constructor<?> getConstructor(final String path, final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getConstructor(parameterClass);
  }

  public static <T> Constructor<T> getConstructor(final Class<T> clazz,
      final Class<?>... parameterClass) throws NoSuchMethodException {
    return clazz.getConstructor(parameterClass);
  }

  public static Constructor<?> getDcConstructor(final String path, final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  public static <T> Constructor<T> getDcConstructor(final Class<T> clazz,
      final Class<?>... parameterClass) throws NoSuchMethodException {
    final Constructor<T> constructor = clazz.getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  public static Constructor<?>[] getConstructors(final String path)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors();
  }

  public static Constructor<?> getConstructors(final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors()[size];
  }

  public static <T> Constructor<T>[] getConstructors(final Class<T> clazz) {
    return Arrays.stream(clazz.getConstructors())
        .toArray(Constructor[]::new);
  }

  public static <T> Constructor<T> getConstructors(final Class<T> clazz, final int size) {
    return Arrays.stream(clazz.getConstructors())
        .toArray(Constructor[]::new)[size];
  }

  public static Constructor<?>[] getDcConstructors(final String path)
      throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  public static Constructor<?> getDcConstructors(final String path, final int size)
      throws ClassNotFoundException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  public static <T> Constructor<T>[] getDcConstructors(final Class<T> clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  public static <T> Constructor<T> getDcConstructors(final Class<T> clazz, final int size) {
    final Constructor<T> constructor = Arrays.stream(clazz.getDeclaredConstructors())
        .toArray(Constructor[]::new)[size];
    constructor.setAccessible(true);
    return constructor;
  }

  public static Method getMethod(final String path, final String methodName,
      final Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getMethod(methodName, parameterClass);
  }

  public static Method getMethod(final Class<?> clazz, final String methodName,
      final Class<?>... parameterClass) throws NoSuchMethodException {
    return clazz.getMethod(methodName, parameterClass);
  }

  public static Method getDcMethod(final String path, final String methodName,
      final Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
    final Method method = getClazz(path).getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  public static Method getDcMethod(final Class<?> clazz, final String methodName,
      final Class<?>... parameterClass) throws NoSuchMethodException {
    final Method method = clazz.getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  public static Method[] getMethods(final String path) throws ClassNotFoundException {
    return getClazz(path).getMethods();
  }

  public static Method getMethods(final String path, final int size) throws ClassNotFoundException {
    return getClazz(path).getMethods()[size];
  }

  public static Method[] getMethods(final Class<?> clazz) {
    return clazz.getMethods();
  }

  public static Method getMethods(final Class<?> clazz, final int size) {
    return clazz.getMethods()[size];
  }

  public static Method[] getDcMethods(final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  public static Method getDcMethods(final String path, final int size)
      throws ClassNotFoundException {
    final Method method = getClazz(path).getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  public static Method[] getDcMethods(final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  public static Method getDcMethods(final Class<?> clazz, final int size) {
    final Method method = clazz.getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  public static Field getField(final String path, final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    return getClazz(path).getField(variableName);
  }

  public static Field getField(final Class<?> clazz, final String variableName)
      throws NoSuchFieldException {
    return clazz.getField(variableName);
  }

  public static Field getDcField(final String path, final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    final Field field = getClazz(path).getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  public static Field getDcField(final Class<?> clazz, final String variableName)
      throws NoSuchFieldException {
    final Field field = clazz.getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  public static Field[] getFields(final String path) throws ClassNotFoundException {
    return getClazz(path).getFields();
  }

  public static Field getFields(final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getFields()[size];
  }

  public static Field[] getFields(final Class<?> clazz) {
    return clazz.getFields();
  }

  public static Field getFields(final Class<?> clazz, final int size) {
    return clazz.getFields()[size];
  }

  public static Field[] getDcFields(final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  public static Field getDcFields(final String path, final int size) throws ClassNotFoundException {
    final Field field = getClazz(path).getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  public static Field[] getDcFields(final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  public static Field getDcFields(final Class<?> clazz, final int size) {
    final Field field = clazz.getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  public static <T extends Annotation> boolean containsAnnotation(final Class<?> clazz,
      final Class<T> annotation) {
    return clazz.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> boolean containsAnnotation(final Constructor<?> constructor,
      final Class<T> annotation) {
    return constructor.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> boolean containsAnnotation(final Field field,
      final Class<T> annotation) {
    return field.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Class<?> clazz,
      final Class<T> annotation) {
    return clazz.getAnnotation(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Constructor<?> constructor,
      final Class<T> annotation) {
    return constructor.getAnnotation(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Field field,
      final Class<T> annotation) {
    return field.getAnnotation(annotation);
  }

  public static Object instance(final Constructor<?> constructor)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance();
  }

  public static Object instance(final Constructor<?> constructor, final Object... args)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(args);
  }

  public static Object invoke(final Method method, final Object instance, final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(instance, args);
  }

  public static Object invokeStatic(final Method method, final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(null, args);
  }

  public static Object get(final Field field, final Object instance)
      throws IllegalAccessException {
    return field.get(instance);
  }

  public static Object getStatic(final Field field) throws IllegalAccessException {
    return field.get(null);
  }

}
