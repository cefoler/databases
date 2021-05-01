package com.celeste.databases.core.model.entity;

import java.io.File;
import java.util.Properties;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
public final class LocalCredentials {

  @NotNull
  private final String name;

  @NotNull
  private final File path;

  @NotNull
  private final Properties other;

}
