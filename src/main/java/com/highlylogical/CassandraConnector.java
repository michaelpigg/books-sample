package com.highlylogical;

import com.datastax.oss.driver.api.core.CqlSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import org.crac.Context;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraConnector implements Resource {
    private FileWriter logFile;
    private static final Logger log = LoggerFactory.getLogger(CassandraConnector.class);
    private CqlSession session;
    private final String[] contactPoints;
    private final String keyspace;

    public CassandraConnector(String[] contactPoints, String keyspace) {
        this.contactPoints = contactPoints;
        this.keyspace = keyspace;
        openLogFile();
    }

    public CqlSession connect() {
        return connect(this.contactPoints, this.keyspace);
    }

    private void openLogFile() {
        try {
            logFile = new FileWriter("connector.log");
        } catch (IOException e) {
            log.error("Unable to open log file", e);
        }
    }

    private void writeLogFile(String message) {
        try {
            logFile.write(ZonedDateTime.now() + " " + message + "\n");
            logFile.flush();
        } catch (IOException e) {
            log.error("Unable to write log file", e);
        }
    }

    public CqlSession connect(String[] contactPoints, String keyspace) {
        if (session == null) {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(contactPoints[0].split(":")[0],
                            Integer.parseInt(contactPoints[0].split(":")[1])))
                    .withKeyspace(keyspace)
                    .withLocalDatacenter("datacenter1") // Adjust for your setup
                    .build();
            log.info("Built new cassandra session " + session);
            writeLogFile("Built new cassandra session" + session);
            System.out.println("Built new cassandra session " + session); 
        }
        return session;
    }

    public void close() {
        writeLogFile("Attempt to close cassandra session " + session);
        if (session != null){
            CompletionStage<Void> cs = session.closeAsync();
            cs.toCompletableFuture().join();
            writeLogFile("Closed cassandra session " + session);
            System.out.println("Closed cassandra session " + session);
            session = null;
	    }
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        writeLogFile("CassandraConnector before checkpoint");
        log.info("Shutdown cassandra session before checkpoint" + session);
        close();
        logFile.close();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        openLogFile();
        writeLogFile("CassandraConnector after restore");
        log.info("Cassandra connector restore after checkpoint");
        CompletableFuture.runAsync(() -> {
            connect();
        });
    }
}
