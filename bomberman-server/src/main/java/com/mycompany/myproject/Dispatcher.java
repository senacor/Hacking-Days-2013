package com.mycompany.myproject;

import static java.lang.System.out;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.Verticle;

/**
 * Json Bridge zum Browser / Push siehe bomberman-client-prototyp 
 * @author tschapitz
 */
public class Dispatcher extends Verticle {

    @Override
    public void start() {
        final Logger log = container.logger();
        log.info("Staring server");

        JsonObject config = container.config();
        log.info("Config:" + config);

        container.deployVerticle(StateVerticle.class.getName());
        container.deployVerticle(StartVerticle.class.getName());
     
        final HttpServer httpServer = vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest req) {
                out.println("Handle Reqest:" + req + " about to send ping...");
            }
        });
        SockJSServer sockServer = vertx.createSockJSServer(httpServer);
        
        // Ja ich weiss, es geht auch via dynamischer config, für die Demo aber so:
        sockServer.bridge(new JsonObject("{\"prefix\" : \"/eventbus\"}"), new JsonArray("[{}]"), new JsonArray(" [{}]"));
        
        httpServer.listen(8080, new Handler<AsyncResult<HttpServer>>() {

            @Override
            public void handle(AsyncResult<HttpServer> event) {
                out.println("surf 's up");                
            }
        });
    }
}
