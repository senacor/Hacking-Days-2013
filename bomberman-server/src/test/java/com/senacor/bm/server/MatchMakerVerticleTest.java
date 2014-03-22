package com.senacor.bm.server;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Created by jchrist on 22.03.14.
 */
public class MatchMakerVerticleTest  extends TestVerticle {


    @Test
    public void testMatchMaker() throws Exception {

        vertx.eventBus().send(MatchMakerVerticle.MATCHMAKER_PLAYER_LIST, new JsonObject(), new Handler<Message<JsonArray>>() {
            @Override
            public void handle(Message<JsonArray> message) {
                assertEquals("There should be no players", 0, message.body().size());
                vertx.eventBus().send(MatchMakerVerticle.MATCHMAKER_PLAYER_REGISTER, new JsonObject().putString("name", "charly"), new Handler<Message<Boolean>>() {
                    @Override
                    public void handle(Message<Boolean> message) {
                        assertTrue("Player should be added", message.body());
                        vertx.eventBus().send(MatchMakerVerticle.MATCHMAKER_PLAYER_LIST, new JsonObject(), new Handler<Message<JsonArray>>() {
                            @Override
                            public void handle(Message<JsonArray> message) {
                                assertEquals("There should be one players", 1, message.body().size());
                                testComplete();
                            }
                        });
                    }
                });
            }
        });




    }

    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();

        container.deployVerticle(MatchMakerVerticle.class.getCanonicalName(), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                if (asyncResult.failed()) {
                    container.logger().error(asyncResult.cause());
                }
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });

//        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
//        // don't have to hardecode it in your tests
//        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
//            @Override
//            public void handle(AsyncResult<String> asyncResult) {
//                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
//                if (asyncResult.failed()) {
//                    container.logger().error(asyncResult.cause());
//                }
//                assertTrue(asyncResult.succeeded());
//                assertNotNull("deploymentID should not be null", asyncResult.result());
//                // If deployed correctly then start the tests!
//                startTests();
//            }
//        });
    }
}
