package com.highlylogical;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.crac.Core;

public class Main {
    public static void main(String[] args) {
        Config config = ConfigFactory.load();
        String[] contactPoints = config.getStringList("cassandra.contactPoints").toArray(new String[0]);
        String keyspace = config.getString("cassandra.keyspace");

        final CassandraConnector cassandraConnector = new CassandraConnector(contactPoints, keyspace);
        final CassandraConnectionManager connectionManager = new CassandraConnectionManager(cassandraConnector);
        BookService bookService = new BookService(cassandraConnector);
        Core.getGlobalContext().register(connectionManager);
        ApiServer apiServer = new ApiServer(bookService);
        Core.getGlobalContext().register(apiServer);
        apiServer.start();
    }
}