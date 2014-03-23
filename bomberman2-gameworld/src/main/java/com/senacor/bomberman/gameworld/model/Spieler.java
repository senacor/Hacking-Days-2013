package com.senacor.bomberman.gameworld.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class Spieler {
    private String playerName;
    private Position position;
    private Position targetPosition;
    private int geschwindigkeitsfaktor;
    private int bombenanzahl;
    private int bombenreichweite;
    private int timeSliceFinishingMovement;
    private int timeSliceReachingNextField;
    private boolean istImSpiel;
    private String direction;

    private int siege;
    private int niederlagen;

    public Spieler(String playerName) {
        this.playerName = playerName;
        geschwindigkeitsfaktor = 1;
        bombenanzahl = 1;
        bombenreichweite = 2;
        siege = 0;
        niederlagen = 0;
        istImSpiel = false;
        timeSliceFinishingMovement = 0;
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public int getSiege() {
        return siege;
    }

    public void setSiege(int siege) {
        this.siege = siege;
    }

    public int getNiederlagen() {
        return niederlagen;
    }

    public void setNiederlagen(int niederlagen) {
        this.niederlagen = niederlagen;
    }

    public String getPlayerName() {
        return playerName;
    }

    public JsonObject toJsonObject() {
        JsonObject player = new JsonObject();
        player.putString("name", playerName);
        JsonObject pos = new JsonObject();
        pos.putNumber("x", position.getX());
        pos.putNumber("y", position.getY());
        player.putObject("position", pos);
        return player;
    }

    public int getTimeSliceFinishingMovement() {
        return timeSliceFinishingMovement;
    }

    public void setTimeSliceFinishingMovement(int timeSliceFinishingMovement) {
        this.timeSliceFinishingMovement = timeSliceFinishingMovement;
    }

    public int getTimeSliceReachingNextField() {
        return timeSliceReachingNextField;
    }

    public void setTimeSliceReachingNextField(int timeSliceReachingNextField) {
        this.timeSliceReachingNextField = timeSliceReachingNextField;
    }

    public boolean isMoving(){
        return timeSliceFinishingMovement > 0;
    }

    public void reachTargetField(){
        this.position = targetPosition;
        this.setTimeSliceReachingNextField(0);
    };

    public void finishMovement(){
        targetPosition = null;
        this.setTimeSliceFinishingMovement(0);
    };

    public JsonObject getUpdateJsonObject(){
        JsonObject result = new JsonObject();
        result.putString("playername", playerName);
        result.putObject("position", position.toJsonObject());
        result.putString("direction", direction);
        return result;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
