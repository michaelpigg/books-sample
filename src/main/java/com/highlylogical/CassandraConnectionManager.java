package com.highlylogical;

import org.crac.Context;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraConnectionManager implements Resource {
    private static final Logger log = LoggerFactory.getLogger(CassandraConnectionManager.class);

    private CassandraConnector connector;

    CassandraConnectionManager(CassandraConnector connector) {
        this.connector = connector;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        log.info("Closing Cassandra connection before checkpoint");
        System.out.println("Closing Cassandra connection before checkpoint");
        connector.close();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        log.info("Restore Cassandra connection after checkpoint");
        System.out.println("Restore Cassandra connection after checkpoint");
        //connector.connect();
    }
}
