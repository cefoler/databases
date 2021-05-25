package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;

public interface Kafka extends Messenger {

  @Override
  default MessengerDao createDao() {
    return null;
  }

}
