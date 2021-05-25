package com.celeste.databases.storage.factory;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.type.AccessType;
import com.celeste.databases.core.model.entity.Credentials;
import com.celeste.databases.storage.model.database.provider.Storage;
import com.celeste.databases.storage.model.database.type.StorageType;
import java.lang.reflect.Constructor;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StorageFactory {

  private static final StorageFactory INSTANCE;

  static {
    INSTANCE = new StorageFactory();
  }

  public Storage start(final Properties properties) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final StorageType storage = StorageType.getStorage(driver);

      final AccessType access = storage.getAccess();
      final Credentials credentials = access.serialize(properties);

      final Constructor<? extends Storage> constructor = storage.getProvider()
          .getConstructor(access.getCredentials());

      return constructor.newInstance(credentials);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  public static StorageFactory getInstance() {
    return INSTANCE;
  }

}
