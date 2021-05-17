package com.celeste.databases.cache.model.database.type;

import com.celeste.databases.cache.model.database.provider.Cache;
import com.celeste.databases.cache.model.database.provider.impl.redis.RedisProvider;
import com.celeste.databases.core.model.database.type.AccessType;
import com.google.common.collect.ImmutableList;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum CacheType {

  REDIS(RedisProvider.class, AccessType.REMOTE, "REDIS", "RE");

  private final Class<? extends Cache> provider;
  private final AccessType access;
  private final List<String> names;

  CacheType(final Class<? extends Cache> provider, final AccessType access,
      final String... names) {
    this.provider = provider;
    this.access = access;
    this.names = ImmutableList.copyOf(names);
  }

  public static CacheType getCache(final String cache) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(cache.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid cache: " + cache));
  }

  public static CacheType getCache(final String cache, @Nullable final CacheType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(cache.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}
