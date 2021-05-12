package com.celeste.databases.core.model.entity;

import java.io.File;
import java.util.Properties;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LocalCredentials {

  private final String name;
  private final File path;

  private final Properties other;

}
