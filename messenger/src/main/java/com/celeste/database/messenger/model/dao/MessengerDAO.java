package com.celeste.database.messenger.model.dao;

import com.celeste.database.shared.model.dao.DAO;
import com.celeste.database.shared.model.database.provider.exception.FailedConnectionException;
import org.jetbrains.annotations.NotNull;

public interface MessengerDAO extends DAO {

  void publish(@NotNull final String message, @NotNull final String channelName) throws FailedConnectionException;

  void subscribe(@NotNull final Object instance, @NotNull final String channelsName) throws FailedConnectionException;

}