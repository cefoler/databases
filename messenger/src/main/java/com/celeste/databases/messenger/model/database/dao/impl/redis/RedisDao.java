package com.celeste.databases.messenger.model.database.dao.impl.redis;

import com.celeste.databases.core.model.database.provider.Database;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.provider.impl.redis.Redis;

public final class RedisDao implements MessengerDao {

  private final Redis messenger;

  public RedisDao(final Redis messenger) {
    this.messenger = messenger;
  }

  @Override
  public Database getDatabase() {
    return messenger;
  }

}
