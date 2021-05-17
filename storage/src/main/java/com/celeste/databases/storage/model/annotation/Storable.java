<<<<<<< HEAD:storage/src/main/java/com/celeste/databases/storage/model/annotation/Storable.java
package com.celeste.databases.storage.model.annotation;
=======
package com.celeste.database.messenger.annotation;
>>>>>>> main:messenger/src/main/java/com/celeste/database/messenger/annotation/Subscribe.java

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

<<<<<<< HEAD:storage/src/main/java/com/celeste/databases/storage/model/annotation/Storable.java
=======
/**
 * The subscribe annotation is used by classes extended of AbstractListener to represent the channel
 * name of the Messenger
 */
>>>>>>> main:messenger/src/main/java/com/celeste/database/messenger/annotation/Subscribe.java
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Storable {

<<<<<<< HEAD:storage/src/main/java/com/celeste/databases/storage/model/annotation/Storable.java
=======
  /**
   * Name of the channel, this value will be subscribed as the channel name along with the class as
   * a MessengerPubSub.
   *
   * @return String
   */
  @NotNull
>>>>>>> main:messenger/src/main/java/com/celeste/database/messenger/annotation/Subscribe.java
  String value();

}
