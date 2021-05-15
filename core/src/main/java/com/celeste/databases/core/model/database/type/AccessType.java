package com.celeste.databases.core.model.database.type;

import com.celeste.databases.core.model.entity.Credentials;
import com.celeste.databases.core.model.entity.impl.LocalCredentials;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.Getter;

@Getter
public enum AccessType {

  LOCAL(LocalCredentials.class, "LOCAL", "FLAT") {
    @Override
    public LocalCredentials serialize(final Properties properties) {
      final String name = properties.getProperty("name");
      final File file = new File(properties.getProperty("path"));

      return LocalCredentials.builder()
          .name(name)
          .path(file)
          .other(properties)
          .build();
    }
  },
  REMOTE(RemoteCredentials.class, "REMOTE", "WEB") {
    @Override
    public RemoteCredentials serialize(final Properties properties) {
      final String hostname = properties.getProperty("hostname");
      final int port = Integer.parseInt(properties.getProperty("port"));

      final String database = properties.getProperty("database");

      final String username = properties.getProperty("username");
      final String password = properties.getProperty("password");

      final boolean ssl = Boolean.parseBoolean(properties.getProperty("ssl"));

      return RemoteCredentials.builder()
          .hostname(hostname)
          .port(port)
          .database(database)
          .username(username)
          .password(password)
          .ssl(ssl)
          .other(properties)
          .build();
    }
  };

  private final Class<? extends Credentials> credential;
  private final List<String> names;

  AccessType(final Class<? extends Credentials> credential, final String... names) {
    this.credential = credential;
    this.names = ImmutableList.copyOf(names);
  }

  public static AccessType getAccess(final String access) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(access.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Invalid access: " + access));
  }

  public abstract <T extends Credentials> T serialize(final Properties properties);

}