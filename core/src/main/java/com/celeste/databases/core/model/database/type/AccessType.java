package com.celeste.databases.core.model.database.type;

import com.celeste.databases.core.model.entity.Credentials;
import com.celeste.databases.core.model.entity.impl.LocalCredentials;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum AccessType {

  LOCAL(LocalCredentials.class, "LOCAL", "FLAT") {
    @Override
    public LocalCredentials serialize(final Properties properties) {
      return LocalCredentials.builder()
          .name(properties.getProperty("name"))
          .path(new File(properties.getProperty("path")))
          .other(properties)
          .build();
    }
  },
  REMOTE(RemoteCredentials.class, "REMOTE", "WEB") {
    @Override
    public RemoteCredentials serialize(final Properties properties) {
      return RemoteCredentials.builder()
          .hostname(properties.getProperty("hostname"))
          .port(Integer.parseInt(properties.getProperty("port")))
          .database(properties.getProperty("database"))
          .username(properties.getProperty("username"))
          .password(properties.getProperty("password"))
          .ssl(Boolean.parseBoolean(properties.getProperty("ssl")))
          .other(properties)
          .build();
    }
  };

  private final Class<? extends Credentials> credentials;
  private final List<String> names;

  AccessType(final Class<? extends Credentials> credentials, final String... names) {
    this.credentials = credentials;
    this.names = ImmutableList.copyOf(names);
  }

  public static AccessType getAccess(final String access) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(access.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid access: " + access));
  }

  public static AccessType getAccess(final String access, @Nullable final AccessType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(access.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

  public abstract <T extends Credentials> T serialize(final Properties properties);

}