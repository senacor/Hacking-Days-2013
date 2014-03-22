package com.senacor.bm.model;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class Spielfeld {

    private int width = 11;
    private int height = 11;
    private String[][] felder;

    public Spielfeld(int width, int height) {
        this.width = width;
        this.height = height;
        this.felder = new String[width][height];
    }

    /**
     *
     * @param x verticale Position.
     * @param y horizontale Position.
     * @param feldart Die Art des Feldes.
     */
    public void setFeld(int x, int y, Feldart feldart) {
        felder[x][y] = feldart.toString();
    }

    /**
     * @param x verticale Position.
     * @param y horizontale Position.
     * @return Die Art des Feldes.
     */
    public String getFeld(int x, int y) {
        return felder[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.putNumber("width", width);
        jsonObject.putNumber("height", height);
        jsonObject.putArray("felder", new JsonArray(felder));
        return jsonObject;
    }
}




