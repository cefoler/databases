package com.celeste.databases.core.model.entity.impl;

import com.celeste.databases.core.model.entity.Credentials;
import java.io.File;
import java.util.Properties;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class LocalCredentials implements Credentials {

  private final String name;
  private final File path;

  private final Properties other;

}
