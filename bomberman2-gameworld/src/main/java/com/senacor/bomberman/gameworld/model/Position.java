package com.senacor.bomberman.gameworld.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public JsonObject toJsonObject() {
        JsonObject pos = new JsonObject();
        pos.putNumber("x", getX());
        pos.putNumber("y", getY());
        return pos;
    }
}