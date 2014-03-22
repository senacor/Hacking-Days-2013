package com.senacor.bomberman.gameworld;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jchrist on 22.03.14.
 */
public class GameWorldVerticle extends Verticle {

    public static final String GAMEWORLD_FULL = "gameworld.full";

    /**
     * String = Name of Player
     */
    private Set<String> playerSet = new HashSet<>();

    @Override
    public void start() {
        container.logger().info("deployed GameWorld-Verticle");

        vertx.eventBus().registerHandler(GAMEWORLD_FULL, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                container.logger().info("received event: " + event.body());
                event.reply("Tested!");
            }
        });
    }
}
