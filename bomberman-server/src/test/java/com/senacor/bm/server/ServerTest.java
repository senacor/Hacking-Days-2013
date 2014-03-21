package com.senacor.bm.server;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Created by jchrist on 21.03.14.
 */
public class ServerTest extends TestVerticle {


    @Test
    public void testWebServer() {

        container.logger().info("Start test testWebServer");

        HttpClient client = vertx.createHttpClient()
                .setHost("localhost")
                .setPort(8282);

        HttpClientRequest request = client.get("test.txt", new Handler<HttpClientResponse>() {
            public void handle(HttpClientResponse resp) {
                container.logger().info("Got a response: " + resp.statusCode());
            }
        });

        request.end();
        testComplete();

    }


    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
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
    }

}
