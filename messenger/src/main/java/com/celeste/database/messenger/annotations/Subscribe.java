package com.celeste.database.messenger.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The subscribe annotation is used by classes extended
 * of AbstractListener to represent the channel name of
 * the Messenger
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

  /**
   * Name of the channel, this value will be subscribed as the
   * channel name along with the class as a MessengerPubSub.
   * @return String
   */
  @NotNull
  String value();

}