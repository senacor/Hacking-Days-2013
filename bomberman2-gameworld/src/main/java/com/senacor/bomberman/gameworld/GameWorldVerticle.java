package com.senacor.bomberman.gameworld;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import com.senacor.bomberman.gameworld.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class GameWorldVerticle extends Verticle {

    public static final String GAME_INIT = "game.initialize";
    public static final String GAME_JOIN = "game.join";
    public static final String GAME_UPDATE = "game.update";
    public int gameIdCounter = 1;

    private Map<Integer,Gameworld> games = new HashMap<Integer, Gameworld>();

    public void start() {
        container.logger().info("started GameWorldVerticle");

        // game.initialize -
        vertx.eventBus().registerHandler(GAME_INIT, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("HANDLER: " + GAME_INIT);

                Integer mapWidth = Gameworld.DEFAULT_MAP_WIDTH;
                try {
                    if(message.body().getInteger("MapWidth")!=null){
                        mapWidth = message.body().getInteger("MapWidth");
                    }
                } catch(NullPointerException e){
                    //use default
                }

                Integer mapHeigth = Gameworld.DEFAULT_MAP_HEIGTH;
                try {
                    if(message.body().getInteger("MapHeight")!=null){
                        mapHeigth = message.body().getInteger("MapHeight");
                    }
                } catch(NullPointerException e){
                    //use default
                }

                JsonArray playerArray = message.body().getArray("Player");

                final Gameworld game = new Gameworld(gameIdCounter++, mapWidth, mapHeigth, playerArray);
                games.put(game.getGameId(), game);

                // reply initial map
                message.reply(game.getGameWorldJsonObject());

                // trigger game start.
                JsonObject participants = new JsonObject();
                participants.putArray("participants", game.getPlayer());
                participants.putNumber("gameId", game.getGameId());

                vertx.eventBus().send("game.start", participants);
                vertx.eventBus().send("dashboard.start", "gameId");
                vertx.eventBus().send("dashboard.init.reply", game.getGameWorldJsonObject());

                container.logger().info("game initialized");
            }
        });

        // game.join -
        vertx.eventBus().registerHandler(GAME_JOIN, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("HANDLER: " + GAME_JOIN);

                if(games.size() == 0){
                    throw new RuntimeException("Es gibt noch kein Spiel!");
                }

                String name = message.body().getString("Name");
                int gameId = message.body().getInteger("GameId");

                if(!games.containsKey(gameId)){
                    throw new RuntimeException("Das Spiel "+ gameId + " gibt es nicht.");
                }
                Gameworld game = games.get(gameId);

                Spieler player = game.enterGameWorld(name);

                // reply initial map
                message.reply(game.getGameWorldJsonObject());

                // update game state.
                JsonObject joinMsg = new JsonObject();
                joinMsg.putObject("player", player.toJsonObject());
                vertx.eventBus().send("game."+gameId+".join", joinMsg);

                container.logger().info("player '" + name + "' joined game " + gameId);
            }
        });

        // game.update
        vertx.eventBus().registerHandler(GAME_UPDATE, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("HANDLER: " + GAME_UPDATE);

                int gameId = message.body().getInteger("gameId");
                if(!games.containsKey(gameId)){
                    throw new RuntimeException("Game "+gameId+" not found!");
                }
                Gameworld game = games.get(gameId);

                JsonArray playerMovements = message.body().getArray("commands");
                JsonObject update = game.update(playerMovements);

                //sendeWorldupdate
                message.reply(update);
                vertx.eventBus().send("game."+gameId+"update.clients", update);

                container.logger().info("game world updated:");
                container.logger().info(update.toString());
            }
        });
    }
}

