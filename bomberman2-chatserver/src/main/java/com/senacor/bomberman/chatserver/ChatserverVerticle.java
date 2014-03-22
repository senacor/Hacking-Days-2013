package com.senacor.bomberman.chatserver;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * Created by jchrist on 22.03.14.
 */
public class ChatserverVerticle extends Verticle {

    public static final String CHATMESSAGE_TOSERVER = "chatmessage.toserver";
    public static final String CHATMESSAGE_TOCLIENT = "chatmessage.toclient";

    private void broadcastMessage(String message) {
        vertx.eventBus().publish(CHATMESSAGE_TOCLIENT, message);
    }

    @Override
    public void start() {

        vertx.eventBus().registerHandler(CHATMESSAGE_TOSERVER, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                logEvent(CHATMESSAGE_TOSERVER);
                broadcastMessage(event.body());
            }
        });

        broadcastMessage("Chat started.");
    }

    private void logEvent(String type) {
        container.logger().info("event occurred: " + type);
        vertx.eventBus().send("hd13.eventlogger", new JsonObject().putString("event-type", type));
    }
}
