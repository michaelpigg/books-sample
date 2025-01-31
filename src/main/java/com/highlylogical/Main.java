package com.highlylogical;

import io.javalin.util.ConcurrencyUtil;
import org.crac.Core;

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
        BookService bookService = new BookService(cassandraConnector);
        ApiServer apiServer = new ApiServer(bookService);
        Core.getGlobalContext().register(apiServer);
        Core.getGlobalContext().register(cassandraConnector);
        apiServer.start();
    }
}
