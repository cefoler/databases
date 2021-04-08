package com.celeste.database.storage.factory;

import com.celeste.database.shared.model.type.ConnectionType;
import com.celeste.database.shared.exceptions.database.FailedConnectionException;
import com.celeste.database.storage.model.database.provider.Storage;
import com.celeste.database.storage.model.database.StorageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StorageFactory {

  private static final StorageFactory INSTANCE = new StorageFactory();

  public Storage startStorage(@NotNull final Properties properties, final ConnectionType connectionType) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final StorageType storage = StorageType.getStorage(driver);

      final Constructor<? extends Storage> constructor = storage.getProvider().getConstructor(Properties.class, ConnectionType.class);
      return constructor.newInstance(properties, connectionType);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  public static StorageFactory getInstance() {
    return INSTANCE;
  }

}