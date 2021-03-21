[![](https://jitpack.io/v/celesteoficial/databases.svg)](https://jitpack.io/#celesteoficial/databases)

# databases

The databases project is a annotation based **Connection provider and Query** for **SQL** and **NoSQL** databases such as **MongoDB, MySQL and Redis**.
The project will have full support for MongoDB, MySQL, POSTGRESQL, H2 and SQLITE.


Here you can find the source code and documentation of this project.

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

First of all, you should create your connection with the database

Usable strings for the type property:


```
MongoDB - "MONGODB", "MONGO"
MySQL - "MYSQL"
POSTGRESQL - "POSTGRESQL", "POSTGRE", "POST", "GRE"
H2 - "H2"
SQLite - "SQLITE", "SQL", "LITE"
```

The connection is created between multiple properties

```
Type: Type of the database, as listed above
Hostname: Hostname of the database, only used for MySQL and PostgreSQL providers
Port: Port for the database access, used in MySQL and PostgreSQL providers
Database: Name of the database, used in MongoDB, MySQL and PostgreSQL providers
Username: Username for the database access, used in MongoDB, MySQL and PostgreSQL providers
Password: Password for the database access, used in MongoDB, MySQL and PostgreSQL providers
Path: Path where it will be stored, used in H2 and SQLite providers
```

ConnectionFactory example:

```
@Getter
public class ConnectionFactory {

    private final Database database;

    public ConnectionFactory() throws FailedConnectionException, DAOException {
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

