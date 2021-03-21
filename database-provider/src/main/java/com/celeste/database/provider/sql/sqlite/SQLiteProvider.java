package com.celeste.database.provider.sql.sqlite;

import com.celeste.database.provider.sql.SQL;
import com.celeste.database.type.DatabaseType;
import com.celeste.exception.FailedConnectionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.jetbrains.annotations.NotNull;
import org.sqlite.jdbc4.JDBC4Connection;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Getter(AccessLevel.PRIVATE)
public final class SQLiteProvider implements SQL {

    private final Properties properties;
    private final String connectionUrl;

    @Getter
    private Connection connection;

    public SQLiteProvider(@NotNull final Properties properties) throws FailedConnectionException {
        this.properties = properties;
        this.connectionUrl = "jdbc:sqlite:{path}";

        init();
    }

    @Override @Synchronized
    public void init() throws FailedConnectionException {
        try {
            Class.forName("org.sqlite.jdbc4.JDBC4Connection");

            final String name = properties.getProperty("name").concat(".db");
            final Path path = Path.of(properties.getProperty("path")).resolve(name);

            this.connection = new JDBC4Connection(
                connectionUrl.replace("{path}", path.toString()),
                path.toString(),
                new Properties()
            );
        } catch (Throwable throwable) {
            throw new FailedConnectionException(throwable);
        }
    }

    @Override @Synchronized @SneakyThrows
    public void shutdown() {
        connection.close();
    }

    @Override @SneakyThrows
    public boolean isConnect() {
        return !connection.isClosed();
    }

    @Override @NotNull
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }

    @Override
    public int executeUpdate(final @NotNull String sql, @NotNull final Object... values) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            applyValues(statement, values);
            return statement.executeUpdate();
        }
    }

    @Override @NotNull
    public ResultSet executeQuery(final @NotNull String sql, @NotNull final Object... values) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(sql);

        applyValues(statement, values);
        return statement.executeQuery();
    }

}
