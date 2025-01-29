package com.highlylogical;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraConnector {
    private static final Logger log = LoggerFactory.getLogger(CassandraConnector.class);
    private static CqlSession session;
    private final String[] contactPoints;
    private final String keyspace;

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
	    log.info("Built new  cassandra session " + session);
            System.out.println("Built new cassandra session " + session); 
        }
        return session;
    }

    public void close() {
        if (session != null){
	       	session.close();
		log.info("Closed cassandra session " + session);
		System.out.println("Closed cassandra session " + session);
		session = null;
	}
    }
}
