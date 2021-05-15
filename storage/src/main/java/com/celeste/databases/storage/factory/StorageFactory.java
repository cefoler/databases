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

  private static final StorageFactory INSTANCE = new StorageFactory();

  public Storage start(final Properties properties) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final StorageType storage = StorageType.getStorage(driver);

      final AccessType access = storage.getAccess();

      final Class<? extends Storage> provider = storage.getProvider();
      final Class<? extends Credentials> credential = access.getCredential();

      final Credentials credentials = access.serialize(properties);

      final Constructor<? extends Storage> constructor = provider.getConstructor(credential);
      return constructor.newInstance(credentials);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  public static StorageFactory getInstance() {
    return INSTANCE;
  }

}
