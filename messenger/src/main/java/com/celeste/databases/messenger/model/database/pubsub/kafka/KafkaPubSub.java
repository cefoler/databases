package com.celeste.databases.messenger.model.database.pubsub.kafka;

import com.celeste.databases.messenger.model.database.provider.impl.kafka.Kafka;
import com.celeste.databases.messenger.model.entity.Listener;
import java.time.Duration;
import java.util.Collection;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public final class KafkaPubSub extends KafkaConsumer<String, String> {

  private final Listener listener;

  public KafkaPubSub(final Kafka kafka, final Listener listener) {
    super(kafka.getProperties());
    this.listener = listener;
  }

  @Override
  public void subscribe(final Collection<String> topics) {
    super.subscribe(topics);

    while (true) {
      final ConsumerRecords<String, String> messages = poll(Duration.ofMillis(1000));
      messages.forEach(message -> listener.receive(message.topic(), message.value()));
    }
  }

}
