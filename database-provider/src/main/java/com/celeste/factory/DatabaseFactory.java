package com.celeste.factory;

import com.celeste.Serializable;
import com.celeste.dao.DAO;
import com.celeste.dao.mongodb.MongoDBDAO;
import com.celeste.dao.sql.SQLDAO;
import com.celeste.database.provider.Database;
import com.celeste.database.provider.mongodb.MongoDB;
import com.celeste.database.provider.mongodb.MongoDBProvider;
import com.celeste.database.provider.sql.SQL;
import com.celeste.database.provider.sql.mysql.MySQLProvider;
import com.celeste.database.type.DatabaseType;
import com.celeste.exception.DAOException;
import com.celeste.exception.DatabaseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseFactory {

    @Getter
    private static final DatabaseFactory instance = new DatabaseFactory();

    @NotNull
    public Database start(@NotNull final Properties properties) throws DatabaseException {
        final String driver = properties.getProperty("driver");
        final DatabaseType database = DatabaseType.getDatabase(driver);

        switch (database) {
            case MYSQL:
                return new MySQLProvider(properties);
            case MONGODB:
                return new MongoDBProvider(properties);
            default:
                return new MySQLProvider(properties);
        }
    }

    @NotNull
    public <T extends Serializable<T>> DAO<T> startDAO(@NotNull final Database database,
                                                       @NotNull final Class<T> clazz) throws DAOException {
        return database.getType() == DatabaseType.MONGODB ?
          new MongoDBDAO<>((MongoDB) database, clazz) :
          new SQLDAO<>((SQL) database, clazz);
    }

}
