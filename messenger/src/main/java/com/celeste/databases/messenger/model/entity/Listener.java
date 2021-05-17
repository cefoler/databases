package com.celeste.databases.messenger.model.entity;

public interface Listener {

  void receive(final String channel, final String message);

}
