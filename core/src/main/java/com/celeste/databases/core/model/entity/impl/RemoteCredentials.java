package com.celeste.databases.core.model.entity.impl;

import com.celeste.databases.core.model.entity.Credentials;
import java.util.Properties;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class RemoteCredentials implements Credentials {

  private final String hostname;
  private final int port;

  private final String database;

  private final String username;
  private final String password;

  private final boolean ssl;

  private final Properties other;

}