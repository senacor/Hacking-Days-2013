package com.senacor.bomberman.gameworld.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class PlacedBomb {
    private Position position;
    private String playerId;
    private int range;

    public JsonObject toJsonObject() {
        JsonObject bomb = new JsonObject();
        JsonObject pos = new JsonObject();
        bomb.putObject("position", position.toJsonObject());
        bomb.putString("playerId", playerId);
        bomb.putNumber("range", range);
        return bomb;
    }
}
