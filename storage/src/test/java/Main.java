import com.celeste.databases.storage.factory.StorageFactory;
import com.celeste.databases.storage.model.database.dao.StorageDao;
import com.celeste.databases.storage.model.database.provider.Storage;
import java.util.Properties;
import java.util.UUID;

public class Main {

  public static void main(final String[] args) {
    try {
      final Properties properties = new Properties();
      properties.setProperty("path", "/home/deser/Downloads");
      properties.setProperty("name", "Database");
      properties.setProperty("driver", "sqlite");

      final Storage storage = StorageFactory.getInstance().start(properties);
      final StorageDao<User> dao = storage.createDao(User.class);

      final User user = new User(UUID.randomUUID(), "ABLUBLE", 0);
      dao.save(user);
    } catch (Throwable throwable) {
      System.out.println(throwable.getMessage());
    }
  }

}
