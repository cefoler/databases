package com.celeste.database.provider.sql.h2;

import com.celeste.database.provider.sql.SQL;
import com.celeste.database.type.DatabaseType;
import com.celeste.exception.FailedConnectionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.h2.jdbc.JdbcConnection;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Getter(AccessLevel.PRIVATE)
public final class H2Provider implements SQL {

    private final Properties properties;
    private final String connectionUrl;

    @Getter
    private Connection connection;

    public H2Provider(@NotNull final Properties properties) throws FailedConnectionException {
        this.properties = properties;
        this.connectionUrl = "jdbc:h2:{path}";

        init();
    }

    @Override @Synchronized
    public void init() throws FailedConnectionException {
        try {
            Class.forName("org.h2.jdbc.JdbcConnection");

            final String name = properties.getProperty("name");
            final Path path = Path.of(properties.getProperty("path")).resolve(name);

            this.connection = new JdbcConnection(
              connectionUrl.replace("{path}", path.toString()),
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
        return DatabaseType.H2;
    }

    @Override
    public int executeUpdate(final @NotNull String sql, @NotNull final Object... values) throws SQLException {
        try (
            final PreparedStatement statement = connection.prepareStatement(sql.replace("REPLACE", "MERGE"))
        ) {
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
