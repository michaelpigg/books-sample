package com.highlylogical;

import io.javalin.util.ConcurrencyUtil;
import org.crac.Core;
import org.crac.Resource;

public class Main {
    public static void main(String[] args) {
        ConcurrencyUtil.INSTANCE.setUseLoom(false);
        final String cassandraHost = System.getenv("CASSANDRA_HOST");
        if (cassandraHost == null) {
            System.out.println("Cassandra host not set");
            System.exit(1);
        }
        String[] contactPoints = new String[]{cassandraHost + ":9042"};
        String keyspace = "bookstore";
        final CassandraConnector cassandraConnector = new CassandraConnector(contactPoints, keyspace);
        final CassandraConnectionManager connectionManager = new CassandraConnectionManager(cassandraConnector);
        BookService bookService = new BookService(cassandraConnector);
        ApiServer apiServer = new ApiServer(bookService);
        ControlServer controlServer = new ControlServer(cassandraConnector);
        Core.getGlobalContext().register(controlServer);
        Core.getGlobalContext().register(apiServer);
        Core.getGlobalContext().register(connectionManager);
        apiServer.start();
    }
}
