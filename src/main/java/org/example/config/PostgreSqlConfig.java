package org.example.config;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public final class PostgreSqlConfig {

    private static final PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost("localhost")
            .setDatabase("vertx")
            .setUser("admin")
            .setPassword("admin");
    private static final PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    private static final PgPool pgPool = PgPool.pool(Vertx.currentContext().owner(), connectOptions, poolOptions);

    private PostgreSqlConfig() {
    }

    public static PgPool getPgPool() {
        return pgPool;
    }
}
