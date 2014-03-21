package com.senacor.bm.server;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class Server extends Verticle {

    @Override
    public void start() {

        final Logger logger = container.logger();

        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest req) {
                String file = req.path().equals("/") ? "index.html" : req.path();
                req.response().sendFile("webroot/" + file);
            }
        }).listen(8080);

        JsonObject config = new JsonObject();
        config.putString("host", "localhost");
        config.putNumber("port", 8282);
        container.deployModule("io.vertx~mod-web-server~2.0.0-final", config);
        logger.info("web-server deployed");

    }
}
