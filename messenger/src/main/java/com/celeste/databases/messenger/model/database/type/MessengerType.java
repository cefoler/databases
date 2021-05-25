package com.celeste.databases.messenger.model.database.type;

import com.celeste.databases.core.model.database.type.AccessType;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.celeste.databases.messenger.model.database.provider.impl.kafka.KafkaProvider;
import com.celeste.databases.messenger.model.database.provider.impl.rabbitmq.RabbitMqProvider;
import com.celeste.databases.messenger.model.database.provider.impl.redis.RedisProvider;
import com.google.common.collect.ImmutableList;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum MessengerType {

  REDIS(RedisProvider.class, AccessType.REMOTE, "REDIS", "RE"),
  RABBITMQ(RabbitMqProvider.class, AccessType.REMOTE, "RABBITMQ", "RABBIT", "RBT"),
  KAFKA(KafkaProvider.class, AccessType.REMOTE, "KAFKA");

  private final Class<? extends Messenger> provider;
  private final AccessType access;
  private final List<String> names;

  MessengerType(final Class<? extends Messenger> provider, final AccessType access,
      final String... names) {
    this.provider = provider;
    this.access = access;
    this.names = ImmutableList.copyOf(names);
  }

  public static MessengerType getMessenger(final String messenger) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(messenger.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new InvalidParameterException("Invalid messenger: " + messenger));
  }

  public static MessengerType getMessenger(final String messenger,
      @Nullable final MessengerType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(messenger.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}
