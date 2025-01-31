package com.highlylogical;

import io.javalin.Javalin;
import org.crac.Context;
import org.crac.Resource;

public class ControlServer implements Resource {
    private final CassandraConnector cassandraConnector;
    private Javalin controlServer;

    public ControlServer(CassandraConnector cassandraConnector) {
        this.cassandraConnector = cassandraConnector;
        start();
    }


    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        System.out.println("Shut down control server");
        controlServer.stop();
        controlServer = null;
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("Start control server after restore");
        start();
    }

    private void start() {
        this.controlServer = Javalin.create().start(7000);
        this.controlServer.post("/shutdown", ctx -> {
            cassandraConnector.close();
            ctx.status(201);
        });
    }

}
