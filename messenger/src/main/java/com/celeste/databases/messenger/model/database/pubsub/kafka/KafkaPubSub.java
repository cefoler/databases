package com.celeste.databases.messenger.model.database.pubsub.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class KafkaPubSub extends KafkaConsumer<String, String> {

  private static final Properties settings;

  static {
    settings = new Properties();
    // TODO: Add more settings
  }

  public KafkaPubSub(final List<String> channels) {
    super(settings);
    subscribe(channels);

    init();
  }

  /**
   * Every 1 second, the consumer receives all records from that time
   * and executes the handle.
   */
  private void init() {
    final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutor.scheduleAtFixedRate(() -> {
      final ConsumerRecords<String, String> messages = poll(Duration.ofMillis(1000));
      messages.forEach(this::handle);
    }, 1, 1, TimeUnit.SECONDS);
  }

  abstract void handle(final ConsumerRecord<String, String> message);

}
