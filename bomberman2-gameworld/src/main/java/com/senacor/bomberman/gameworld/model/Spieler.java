package com.senacor.bomberman.gameworld.model;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class Spieler {
    private String playerName;
    private Position position;
    private int geschwindigkeitsfaktor;
    private int bombenanzahl;
    private int bombenreichweite;
    private boolean istInBewegung;
    private boolean istImSpiel;

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
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
}
