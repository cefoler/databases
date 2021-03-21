[![](https://jitpack.io/v/celesteoficial/databases.svg)](https://jitpack.io/#celesteoficial/databases)

# databases

The databases project is a annotation based **Connection provider and Query** for **SQL** and **NoSQL** databases such as **MongoDB, MySQL and Redis**.
Here you can find the source code and documentation of this project.

The project will have full support for MongoDB, MySQL, POSTGRESQL, H2 and SQLITE.

# How to install

Maven

```
<dependency>
	 <groupId>com.github.celesteoficial</groupId>
	 <artifactId>databases</artifactId>
	 <version>VERSION</version>
 </dependency>
```

Gradle

`implementation 'com.github.celesteoficial:databases:VERSION'`

# Using the API

To use the API, you should create a ConnectionFactory to access the database with the credentials.
The connection is created between multiple properties, described below

```
Type: Type of the database
Hostname: Hostname of the database, only used for MySQL and PostgreSQL providers
Port: Port for the database access, used in MySQL and PostgreSQL providers
Database: Name of the database, used in MongoDB, MySQL and PostgreSQL providers
Username: Username for the database access, used in MongoDB, MySQL and PostgreSQL providers
Password: Password for the database access, used in MongoDB, MySQL and PostgreSQL providers
Path: Path where it will be stored, used in H2 and SQLite providers
```

Compatible names for the type property:


```
MongoDB - "MONGODB", "MONGO"
MySQL - "MYSQL"
POSTGRESQL - "POSTGRESQL", "POSTGRE", "POST", "GRE"
H2 - "H2"
SQLite - "SQLITE", "SQL", "LITE"
```

Alright! Now you already know what properties this API uses to create the connection and what properties each type of database uses, now let's check the ConnectionFactory example

```
@Getter
public class ConnectionFactory {

    private final Database database;

    public ConnectionFactory() throws FailedConnectionException {
        this.database = DatabaseFactory.getInstance().startDatabase(
          new PropertiesBuilder()
            .with("type", "YOUR_DATABASE")
            .with("hostname", "EXAMPLE_HOSTNAME")
            .with("port", "3306")
            .with("database", "EXAMPLE_DATABASE")
            .with("username", "USERNAME_HERE")
            .with("password", "A_GREAT_PASSWORD")
            .with("path", getDataFolder().toPath().toAbsolutePath())
            .wrap()
        );
    }

}
```

After instanciating the ConnectionFactory, the connection will be created! Now, how do we create DAOs?

# DAO

The databases API provides a default **DAO**, when using that, you should only specify what Query are you using for that type of method.

```
private final DAO<User> dao;

public UserDAO(final BukkitPlugin plugin, final Database database) throws DAOException {
    this.plugin = plugin;
    this.dao = DatabaseFactory.getInstance().startDAO(database, User.class);

    createTable();
}

@Query(
"CREATE TABLE IF NOT EXISTS `user` (" +
"`id` CHAR(36) NOT NULL PRIMARY KEY, " +
"`name` CHAR(16) NOT NULL, " +
"`credits` DOUBLE NOT NULL);"
)
public void createTable() {
    dao.createTable();
}
```

The startDAO method while creating a new DAO<User> is used like this:

```
@NotNull
    public <T extends Serializable<T>> DAO<T> startDAO(@NotNull final Database database, @NotNull final Class<T> clazz) throws DAOException {
        switch (database.getType()) {
            case MONGODB:
                return new MongoDBDAO<>((MongoDB) database, clazz);
            default:
                return new SQLDAO<>((SQL) database, clazz);
        }
    }
```

After that, you have a perfectly working DAO for your Object, then it will get all information needed with the default database set on your connection factory.
