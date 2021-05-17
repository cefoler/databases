package com.celeste.database.messenger.model.database;

import com.celeste.database.messenger.model.database.provider.Messenger;
import com.celeste.database.messenger.model.database.provider.rabbitmq.RabbitMQProvider;
import com.celeste.database.messenger.model.database.provider.redis.RedisProvider;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * The MessengerType contains all possible types of databases that this framework can access and
 * establish a connection.
 */
@Getter
public enum MessengerType {

  REDIS(RedisProvider.class, "REDIS"),
  RABBITMQ(RabbitMQProvider.class, "RABBITMQ", "RABBITMQ");

  @NotNull
  private final Class<? extends Messenger<?>> provider;

  @NotNull
  private final List<String> names;

  MessengerType(@NotNull final Class<? extends Messenger<?>> provider,
      @NotNull final String... names) {
    this.provider = provider;
    this.names = ImmutableList.copyOf(names);
  }

  @NotNull
  public static MessengerType getMessenger(@NotNull final String cache) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(cache.toUpperCase()))
        .findFirst()
        .orElse(REDIS);
  }

}