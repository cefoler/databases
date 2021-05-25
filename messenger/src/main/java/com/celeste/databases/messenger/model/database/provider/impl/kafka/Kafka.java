package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.dao.impl.kafka.KafkaDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;

public interface Kafka extends Messenger {

  KafkaProducer<String, String> getProducer();

  Properties getProperties();

  @Override
  default MessengerDao createDao() {
    return new KafkaDao(this);
  }

}
