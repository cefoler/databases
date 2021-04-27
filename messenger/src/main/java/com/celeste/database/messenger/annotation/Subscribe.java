package com.celeste.database.messenger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/**
 * The subscribe annotation is used by classes extended of AbstractListener to represent the channel
 * name of the Messenger
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

  /**
   * Name of the channel, this value will be subscribed as the channel name along with the class as
   * a MessengerPubSub.
   *
   * @return String
   */
  @NotNull
  String value();

}