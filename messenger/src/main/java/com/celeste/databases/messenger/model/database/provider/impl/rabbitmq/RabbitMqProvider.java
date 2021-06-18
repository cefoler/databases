package com.celeste.databases.messenger.model.database.provider.impl.rabbitmq;

import com.celeste.databases.core.model.database.provider.exception.FailedConnectionException;
import com.celeste.databases.core.model.database.provider.exception.FailedShutdownException;
import com.celeste.databases.core.model.entity.impl.RemoteCredentials;
import com.celeste.databases.messenger.model.database.type.MessengerType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class RabbitMqProvider implements RabbitMq {

  private final RemoteCredentials credentials;
  private Connection connection;

  public RabbitMqProvider(final RemoteCredentials credentials) throws FailedConnectionException {
    this.credentials = credentials;

    init();
  }

  @Override
  public synchronized void init() throws FailedConnectionException {
    try {
      Class.forName("com.rabbitmq.client.impl.recovery.AutorecoveringConnection");

      final ConnectionFactory factory = new ConnectionFactory();

      factory.setHost(credentials.getHostname());
      factory.setPort(credentials.getPort());

      factory.setVirtualHost("/");

      factory.setUsername(credentials.getUsername());
      factory.setPassword(credentials.getPassword());

      factory.setAutomaticRecoveryEnabled(true);
      factory.setTopologyRecoveryEnabled(false);
      factory.setNetworkRecoveryInterval(30);

      final ExecutorService executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 5L,
          TimeUnit.MINUTES, new SynchronousQueue<>());
      this.connection = factory.newConnection(executor);
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public synchronized void shutdown() throws FailedShutdownException {
    try {
      connection.close();
    } catch (Exception exception) {
      throw new FailedShutdownException(exception);
    }
  }

  @Override
  public boolean isClosed() {
    try {
      return !connection.isOpen();
    } catch (Exception exception) {
      return true;
    }
  }

  @Override
  public Channel getChannel() throws FailedConnectionException {
    try {
      return connection.createChannel();
    } catch (Exception exception) {
      throw new FailedConnectionException(exception);
    }
  }

  @Override
  public MessengerType getMessengerType() {
    return MessengerType.RABBITMQ;
  }

}
