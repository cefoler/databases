package com.celeste.databases.messenger.factory;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.AccessType;
import com.celeste.databases.core.model.entity.Credentials;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import java.lang.reflect.Constructor;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessengerFactory {

  private static final MessengerFactory INSTANCE;

  static {
    INSTANCE = new MessengerFactory();
  }

  public Messenger start(final Properties properties) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final MessengerType messenger = MessengerType.getMessenger(driver);

      final AccessType access = messenger.getAccess();
      final Credentials credentials = access.serialize(properties);

      final Constructor<? extends Messenger> constructor = messenger.getProvider()
          .getConstructor(access.getCredentials());

      return constructor.newInstance(credentials);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  public static MessengerFactory getInstance() {
    return INSTANCE;
  }

}
