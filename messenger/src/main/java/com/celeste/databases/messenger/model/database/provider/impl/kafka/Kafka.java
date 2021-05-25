package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import org.apache.kafka.clients.producer.KafkaProducer;

public interface Kafka extends Messenger {

  KafkaProducer<String, String> getProducer();

  @Override
  default MessengerDao createDao() {
    return null;
  }

}
