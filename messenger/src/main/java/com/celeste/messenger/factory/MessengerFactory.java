package com.celeste.messenger.factory;

import com.celeste.messenger.model.database.provider.Messenger;
import com.celeste.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.messenger.model.database.type.MessengerType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessengerFactory {

  private static final MessengerFactory INSTANCE = new MessengerFactory();

  public Messenger<?> startMessenger(@NotNull final Properties properties) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final MessengerType cache = MessengerType.getMessenger(driver);

      final Constructor<? extends Messenger<?>> constructor = cache.getProvider().getConstructor(Properties.class);
      return constructor.newInstance(properties);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  public static MessengerFactory getInstance() {
    return INSTANCE;
  }

}