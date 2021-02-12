package com.celeste.sql;

import com.celeste.sql.provider.HikariConnectionProvider;
import com.celeste.sql.provider.SQLConnectionProvider;
import com.celeste.sql.util.PropertiesBuilder;

public class SQLProcessor {

    public SQLConnectionProvider sqlConnectionProvider;

    public SQLProcessor() {
        this.sqlConnectionProvider = new HikariConnectionProvider();
    }

    public void startDatabase(
      String host, String port, String database, String user, String password
    ) {
        sqlConnectionProvider.connect(new PropertiesBuilder()
          .with("driver", "mysql")
          .with("hostname", host)
          .with("port", port)
          .with("database", database)
          .with("username", user)
          .with("password", password)
          .wrap());
    }

    public SQLConnectionProvider getSqlConnectionProvider() {
        return sqlConnectionProvider;
    }

}
