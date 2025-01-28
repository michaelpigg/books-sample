package com.highlylogical;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraConnector {
    private CqlSession session;
    private String[] contactPoints;
    private String keyspace;

    public CassandraConnector(String[] contactPoints, String keyspace) {
        this.contactPoints = contactPoints;
        this.keyspace = keyspace;
    }

    public CqlSession connect() {
        return connect(this.contactPoints, this.keyspace);
    }

    public CqlSession connect(String[] contactPoints, String keyspace) {
        if (session == null) {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(contactPoints[0].split(":")[0],
                            Integer.parseInt(contactPoints[0].split(":")[1])))
                    .withKeyspace(keyspace)
                    .withLocalDatacenter("datacenter1") // Adjust for your setup
                    .build();
            session.isClosed();
        }
        return session;
    }

    public void close() {
        if (session != null) session.close();
    }
}