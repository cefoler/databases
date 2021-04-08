package com.celeste.database.storage.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

  @NotNull
  String value();

}