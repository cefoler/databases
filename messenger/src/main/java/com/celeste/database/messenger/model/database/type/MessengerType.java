package com.celeste.database.messenger.model.database.type;

import com.celeste.database.messenger.model.database.provider.Messenger;
import com.celeste.database.messenger.model.database.provider.rabbitmq.RabbitMQProvider;
import com.celeste.database.messenger.model.database.provider.redis.RedisProvider;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MessengerType {

  REDIS(RedisProvider.class, "REDIS"),
  RABBITMQ(RabbitMQProvider.class, "RABBITMQ", "RABBITMQ");

  @NotNull
  private final Class<? extends Messenger<?>> provider;

  @NotNull
  private final List<String> names;

  MessengerType(@NotNull final Class<? extends Messenger<?>> provider, @NotNull final String... names) {
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