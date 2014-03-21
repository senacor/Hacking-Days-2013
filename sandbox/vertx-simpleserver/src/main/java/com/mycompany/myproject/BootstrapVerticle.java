package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * Created by jchrist on 21.03.14.
 */
public class BootstrapVerticle extends Verticle {

    public void start() {

        final Logger logger = container.logger();

        JsonObject config = new JsonObject();
        config.putString("host", "localhost");
        config.putNumber("port", 8181);

        container.deployModule("io.vertx~mod-web-server~2.0.0-final", config);

        logger.info("web-server deployed");

    }

}
