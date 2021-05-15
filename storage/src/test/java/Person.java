import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Name;
import com.celeste.databases.storage.model.annotation.Storable;
import java.util.UUID;

@Storable("person")
public final class Person {

  @Key
  private final String name;
  private final int age;

  @Name("uuid")
  private final UUID id;

  public Person() {
    this.name = null;
    this.age = 0;
    this.id = null;
  }

  public Person(final String name, final int age, final UUID id) {
    this.name = name;
    this.age = age;
    this.id = id;
  }

}
