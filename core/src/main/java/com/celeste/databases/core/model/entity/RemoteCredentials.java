package com.celeste.databases.core.model.entity;

import java.util.Properties;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
public final class RemoteCredentials {

  @NotNull
  private final String hostname;
  private final int port;

  @NotNull
  private final String database;

  @NotNull
  private final String username;

  @NotNull
  private final String password;

  private final boolean ssl;

  @NotNull
  private final Properties other;

}