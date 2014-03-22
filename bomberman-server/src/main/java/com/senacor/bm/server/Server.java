package com.senacor.bm.server;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.Verticle;

import com.senacor.bm.server.multimessagehandler.ResultCollectingHandler;
import com.senacor.bm.server.multimessagehandler.ResultConcatenatingHandler;
import com.senacor.bm.server.multimessagehandler.ResultKeepingHandler;

public class Server extends Verticle {

    private static final String WEBROOT = "web/";

    private Handler<HttpServerRequest> createExampleMultiHandler() {
        return new Handler<HttpServerRequest>() {

            @Override
            public void handle(final HttpServerRequest request) {

                final ResultKeepingHandler<String> playerNameHandler = new ResultKeepingHandler<String>();
                final ResultKeepingHandler<Integer> pointsHandler = new ResultKeepingHandler<Integer>();

                final ResultConcatenatingHandler concatenator = new ResultConcatenatingHandler() {
                    @Override
                    protected String concatenateResults() {
                        return playerNameHandler.getResultMessage() + ": " + pointsHandler.getResultMessage();
                    }
                };

                final ResultCollectingHandler collector = new ResultCollectingHandler(concatenator, request);
                collector.addResultKeepingHandlers(playerNameHandler, pointsHandler);

                vertx.eventBus().send(StateVerticle.ADDR_PLAYERNAME, (String) null, playerNameHandler);
                vertx.eventBus().send(StateVerticle.ADDR_POINTS, (Integer) null, pointsHandler);

            }
        };
    }

    private Handler<HttpServerRequest> createStaticHandler() {
        return new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest req) {
                String file = req.path().equals("/") ? "index.html" : req.path();
                req.response().sendFile(WEBROOT + file);
            }
        };
    }

    private RouteMatcher createRouteMatcher() {
        RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.get("/multi", createExampleMultiHandler());
        routeMatcher.noMatch(createStaticHandler());
        return routeMatcher;
    }

    @Override
    public void start() {

        final Logger logger = container.logger();
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(createRouteMatcher());
        
        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);

        JsonArray permitted = new JsonArray().add(new JsonObject());
        JsonObject sjsConfig = new JsonObject().putString("prefix", "/eventbus");
       
        sockJSServer.bridge(sjsConfig, permitted, permitted);
        
        container.deployVerticle(StateVerticle.class.getCanonicalName(), new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                logger.info("state verticle deployed: "  + event.succeeded());                
            }
        });

        httpServer.listen(8080);

//        JsonObject config = new JsonObject();
//        config.putString("host", "localhost");
//        config.putNumber("port", 8282);
//        container.deployModule("io.vertx~mod-web-server~2.0.0-final", config);
        logger.info("web-server deployed");

    }
}
