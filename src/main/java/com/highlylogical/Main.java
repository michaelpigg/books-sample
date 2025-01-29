package com.highlylogical;

import org.crac.Core;

public class Main {
    public static void main(String[] args) {
        String[] contactPoints = new String[]{"127.0.0.1:9042"}; 
        String keyspace = "bookstore"; 

        final CassandraConnector cassandraConnector = new CassandraConnector(contactPoints, keyspace);
        final CassandraConnectionManager connectionManager = new CassandraConnectionManager(cassandraConnector);
        BookService bookService = new BookService(cassandraConnector);
        ApiServer apiServer = new ApiServer(bookService);
        Core.getGlobalContext().register(apiServer);
        Core.getGlobalContext().register(connectionManager);
        apiServer.start();
    }
}
