package com.celeste.databases.messenger.model.database.provider.impl.kafka;

import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.messenger.model.database.dao.MessengerDao;
import com.celeste.databases.messenger.model.database.dao.impl.kafka.KafkaDao;
import com.celeste.databases.messenger.model.database.provider.Messenger;
import com.celeste.databases.messenger.model.database.pubsub.kafka.KafkaPubSub;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.List;

public interface Kafka extends Messenger {

  KafkaProducer<String, String> getProducer();

  List<KafkaPubSub> getSubscribedChannels();

  RemoteCredentials getCredentials();

  @Override
  default MessengerDao createDao() {
    return new KafkaDao(this);
  }

}
