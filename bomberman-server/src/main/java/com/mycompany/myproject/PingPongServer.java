package com.mycompany.myproject;

import com.senacor.bm.server.*;
import com.mycompany.myproject.PingVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;
import static java.lang.System.out;

/**
 *  Triggern eines anderen Verticles.
 */
public class PingPongServer extends Verticle {

    @Override
    public void start() {
        final Logger log = container.logger();
        log.info("Staring server");

        JsonObject config = container.config();
        log.info("Config:" + config);

        final String DOCUMENT_ROOT = config.getString("RootDir");
        final String PAGE_404 = DOCUMENT_ROOT + "404.html";
        
        container.deployWorkerVerticle(PingVerticle.class.getName());

        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest req) {
                out.println("Handle Reqest:" + req + " about to send ping...");

                vertx.eventBus().send("ping-address", "ping!", new Handler<Message<String>>() {
                    @Override
                    public void handle(Message<String> reply) {
                        out.println("handle message" + reply);
                        req.response().setChunked(true);
                        req.response().write(reply.body());
                        req.response().end();
                        out.println("forwarded message" + reply.body());
                    }
                });
                out.println("... ping sent");

            }
        }).listen(8080);
    }
}
