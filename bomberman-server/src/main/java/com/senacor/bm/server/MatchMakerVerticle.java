package com.senacor.bm.server;

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
                String name = event.body().getString("name");
                boolean successfullyAdded = playerSet.add(name);
                event.reply(successfullyAdded);
            }
        });

        vertx.eventBus().registerHandler(MATCHMAKER_PLAYER_UNREGISTER, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                String name = event.body().getString("name");
                boolean successfullyRemoved = playerSet.remove(name);
                event.reply(successfullyRemoved);
            }
        });


        vertx.eventBus().registerHandler(MATCHMAKER_PLAYER_LIST, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                String[] playerArray = playerSet.toArray(new String[0]);
                event.reply(new JsonArray(playerArray));
            }
        });


    }
}
