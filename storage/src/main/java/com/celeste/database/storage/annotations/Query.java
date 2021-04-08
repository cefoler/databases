package com.celeste.database.storage.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * The annotation is used by SQL methods to define the
 * query used by that method.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

  /**
   * String of the query that is going to be
   * executed by the Method
   *
   * @return String
   */
  @NotNull
  String value();

}