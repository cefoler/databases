package com.celeste.storage.factory;

import com.celeste.shared.model.database.provider.exception.FailedConnectionException;
import com.celeste.storage.model.database.provider.Storage;
import com.celeste.storage.model.database.type.StorageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StorageFactory {

  private static final StorageFactory INSTANCE = new StorageFactory();

  public Storage startStorage(@NotNull final Properties properties) throws FailedConnectionException {
    try {
      final String driver = properties.getProperty("driver");
      final StorageType storage = StorageType.getStorage(driver);

      final Constructor<? extends Storage> constructor = storage.getProvider().getConstructor(Properties.class);
      return constructor.newInstance(properties);
    } catch (Throwable throwable) {
      throw new FailedConnectionException(throwable);
    }
  }

  public static StorageFactory getInstance() {
    return INSTANCE;
  }

}