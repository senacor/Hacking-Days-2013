package com.senacor.bomberman.chatserver;

import static org.vertx.testtools.VertxAssert.*;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.testtools.TestVerticle;

public class ChatserverVerticleTest extends TestVerticle {

    private static final String MESSAGE = "Hallo";
    private static final int BROADCAST_COUNT = 20;

    private int receivedCount = 0;

    private Handler<Message<String>> chatClient = new Handler<Message<String>>() {
        @Override
        public void handle(Message<String> event) {
            assertEquals(MESSAGE, event.body());
            receivedCount++;
            if (receivedCount == BROADCAST_COUNT) {
                testComplete();
            }
        }
    };

    @Test
    public void testChatserver() throws Exception {
        for (int i = 0; i < BROADCAST_COUNT; i++) {
            vertx.eventBus().registerHandler(ChatserverVerticle.CHATMESSAGE_TOCLIENT, chatClient);
        }
        vertx.eventBus().send(ChatserverVerticle.CHATMESSAGE_TOSERVER, MESSAGE);

    }

    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so
        // assert functionality works correctly
        initialize();

        container.deployVerticle(ChatserverVerticle.class.getCanonicalName(), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be
                // called when it's complete (or failed)
                if (asyncResult.failed()) {
                    container.logger().error(asyncResult.cause());
                }
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });
        
        
        

    }
}
