package com.celeste.factory;

import com.celeste.Serializable;
import com.celeste.dao.DAO;
import com.celeste.dao.mongodb.MongoDBDAO;
import com.celeste.dao.sql.SQLDAO;
import com.celeste.database.provider.Database;
import com.celeste.database.provider.mongodb.MongoDB;
import com.celeste.database.provider.mongodb.MongoDBProvider;
import com.celeste.database.provider.sql.SQL;
import com.celeste.database.provider.sql.h2.H2Provider;
import com.celeste.database.provider.sql.mysql.MySQLProvider;
import com.celeste.database.provider.sql.postgresql.PostgreSQLProvider;
import com.celeste.database.provider.sql.sqlite.SQLiteProvider;
import com.celeste.database.type.DatabaseType;
import com.celeste.exception.DAOException;
import com.celeste.exception.FailedConnectionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseFactory {

    @Getter
    private static final DatabaseFactory INSTANCE = new DatabaseFactory();

    @NotNull
    public Database start(@NotNull final Properties properties) throws FailedConnectionException {
        final String type = properties.getProperty("type");
        final DatabaseType database = DatabaseType.getDataBase(type);

        switch (database) {
            case MONGODB:
                return new MongoDBProvider(properties);
            case MYSQL:
                return new MySQLProvider(properties);
            case POSTGRESQL:
                return new PostgreSQLProvider(properties);
            case H2:
                return new H2Provider(properties);
            default:
                return new SQLiteProvider(properties);
        }
    }

    @NotNull
    public <T extends Serializable<T>> DAO<T> createDAO(@NotNull final Database database, @NotNull final Class<T> clazz) throws DAOException {
        switch (database.getType()) {
            case MONGODB:
                return new MongoDBDAO<>((MongoDB) database, clazz);
            default:
                return new SQLDAO<>((SQL) database, clazz);
        }
    }

}
