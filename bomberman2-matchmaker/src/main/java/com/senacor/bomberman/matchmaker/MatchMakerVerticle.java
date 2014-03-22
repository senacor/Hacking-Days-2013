package com.senacor.bomberman.matchmaker;

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
public class MatchMakerVerticle extends Verticle {

    public static final String MATCHMAKER_PLAYER_REGISTER = "matchmaker.player.register";
    public static final String MATCHMAKER_PLAYER_UNREGISTER = "matchmaker.player.unregister";
    public static final String MATCHMAKER_PLAYER_LIST = "matchmaker.player.list";

    /**
     * String = Name of Player
     */
    private Set<String> playerSet = new HashSet<>();

    @Override
    public void start() {

        vertx.eventBus().registerHandler(MATCHMAKER_PLAYER_REGISTER, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                logEvent(MATCHMAKER_PLAYER_REGISTER);
                String name = event.body().getString("name");
                boolean successfullyAdded = playerSet.add(name);
                event.reply(successfullyAdded);
            }
        });

        vertx.eventBus().registerHandler(MATCHMAKER_PLAYER_UNREGISTER, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                logEvent(MATCHMAKER_PLAYER_UNREGISTER);
                String name = event.body().getString("name");
                boolean successfullyRemoved = playerSet.remove(name);
                event.reply(successfullyRemoved);
            }
        });


        vertx.eventBus().registerHandler(MATCHMAKER_PLAYER_LIST, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                logEvent(MATCHMAKER_PLAYER_LIST);
                String[] playerArray = playerSet.toArray(new String[0]);
                event.reply(new JsonArray(playerArray));
            }
        });


        vertx.eventBus().registerHandler("ping", new Handler<Message>() {
            @Override
            public void handle(Message event) {
                container.logger().info("ping!!!");
            }
        });

        // Test startup
        vertx.eventBus().send(MATCHMAKER_PLAYER_LIST, new JsonObject());

        System.out.println("bla");
    }


    private void logEvent(String type) {
        System.out.println("event occurred: " + type);
        container.logger().info("event occurred: " + type);
        vertx.eventBus().send("hd13.eventlogger", new JsonObject()
                .putString("event-type", type));
    }
}
