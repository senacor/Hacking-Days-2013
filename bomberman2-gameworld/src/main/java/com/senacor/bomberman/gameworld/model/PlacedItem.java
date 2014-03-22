package com.senacor.bomberman.gameworld.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class PlacedItem {
    private Position position;

    private Itemtyp itemtyp;

    public PlacedItem(Position position, Itemtyp itemtyp) {
        this.position = position;
        this.itemtyp = itemtyp;
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.putString("type", itemtyp.toString());
        result.putObject("position", position.toJsonObject());
        return result;
    }
}
