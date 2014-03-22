package com.senacor.bomberman.gameworld;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import com.senacor.bomberman.gameworld.model.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by jchrist on 22.03.14.
 */
public class GameWorldVerticle extends Verticle {

    public static final String GAME_INIT = "game.initialize";
    public static final String GAME_UPDATE = "game.update";

    public static final int DEFAULT_MAP_WIDTH = 11;
    public static final int DEFAULT_MAP_HEIGTH = 11;

    /**
     * String = Name of Player
     */
    private Set<String> playerSet = new HashSet<String>();

    private Spielfeld spielfeld;
    private List<Spieler> spieler = new LinkedList<Spieler>();
    private List<PlacedBomb> platzierteBomben = new LinkedList<PlacedBomb>();
    private List<PlacedItem> platzierteItem = new LinkedList<PlacedItem>();

    public void start() {
        container.logger().info("started GameWorldVerticle");

        vertx.eventBus().registerHandler("game.initialize", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("initializing game");

                Integer mapWidth = DEFAULT_MAP_WIDTH;
                try {
                    if(message.body().getInteger("MapWidth")!=null){
                        mapWidth = message.body().getInteger("MapWidth");
                    }
                } catch(NullPointerException e){
                    //use default
                }

                Integer mapHeigth = DEFAULT_MAP_HEIGTH;
                try {
                    if(message.body().getInteger("MapHeight")!=null){
                        mapHeigth = message.body().getInteger("MapHeight");
                    }
                } catch(NullPointerException e){
                    //use default
                }

                JsonArray playerArray = message.body().getArray("Player");

                initializeGameWorld(mapWidth, mapHeigth, playerArray);

                // reply initial map
                message.reply(getGameWorldJsonObject());

                // trigger game start.
                JsonObject participants = new JsonObject();
                participants.putArray("participants", getPlayer());
                vertx.eventBus().send("game.start", participants);

                container.logger().info("game initialized");
            }
        });

        vertx.eventBus().registerHandler(GAME_UPDATE, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                //Zeitabschnitt aus und Prüfe

                //Bewegungen abshließen

                //für alle Bewegungsupdates
                BewegungSpieler();

                //für alle bewegenden Nutzer, die fällig sind:
                PositionswechselNutzer();
                bewegungNutzerAbgeschlossen();

                //Bomben explodieren lassen

                //für alle Bomben, die explodieren müssen
                explosionBombe();

                //für alle bombenPlatzierungsupdates:
                platzierungBombe();
                //setze neuen Zeitabschnitt

                //sendeWorldupdate

            }
        });
    }

    private void initializeGameWorld(Integer mapWidth, Integer mapHeigth, JsonArray playerArray) {

        createPlayerList(playerArray);
        createMap(mapWidth.intValue(), mapHeigth.intValue());
        placePlayersOnTheMap();
    }

    public void createPlayerList(JsonArray playerArray) {
        // Add player
        if ((playerArray != null) && playerArray.size()>0) {
            for (Object playername : playerArray){
                spieler.add(new Spieler(playername.toString()));
            }
        } else {
            Spieler player = new Spieler("Bomberman");
            spieler.add(player);
        }
    }

    private JsonObject getGameWorldJsonObject() {
        JsonObject fullGameWorld = new JsonObject();
        fullGameWorld.putArray("player", getPlayer());
        fullGameWorld.putArray("bombs", getBombs());
        fullGameWorld.putArray("items", getItems());
        fullGameWorld.putObject("map", spielfeld.toJsonObject());
        return fullGameWorld;
    }

    private JsonArray getItems() {
        JsonArray result = new JsonArray();
        for(PlacedItem i : platzierteItem){
            result.add(i.toJsonObject());
        }
        return result;
    }

    private JsonArray getBombs() {
        JsonArray result = new JsonArray();
        for(PlacedBomb b : platzierteBomben){
            result.add(b.toJsonObject());
        }
        return result;
    }

    private JsonArray getPlayer() {
        JsonArray result = new JsonArray();
        for(Spieler p : spieler){
            result.add(p.toJsonObject());
        }
        return result;
    }

    public void BewegungSpieler() {
        // Stelle sicher, dass sich der Spieler nicht bewegt
        // Prüfe ob, Zielposition frei ist (keine Wand und Bombe)

        // Broadcast „Spielerbewegung“ für Animation

        // Timer  einstellen (halbe Bewegungszeit): Positionswechsel
        // Event Positionswechsel
        // Timer einstellen (volle Bewegungszeit): Bewegung abgeschlossen

    }

    public void PositionswechselNutzer() {
        // Setze neue Position
        // Prüfe Feld auf Item
        //      - Auswertung des Items und Aktualisierung der Nutzerattribute
        //      - Broadcast  „Item entfernen“

    }

    public void bewegungNutzerAbgeschlossen() {
        // 	Attribut vom Spieler auf abgeschlossen setzen
    }

    public void platzierungBombe() {
        // Prüfe auf Anzahl der Bomben des Spielers
        // Prüfe, dass es noch keine Bombe an der Position gibt
        // Setze Timer für Explosion
        // Event „Bombe explodiert“
        // Brodacast an alle „Bombe gesetzt“
    }

    public void explosionBombe() {
        // Ermittlung betroffener Spieler
        //	Ermittlung betroffener Bomben
        //	Ermittlung betroffener Wände
        //	Aktualisiere Karte
        //	Generiere Items
        //	Broadcast „neues Item“
        //	Broadcast Bombe explodiert
        //	Setze Timer für Bombenexplosion
        //	Event Bombe explodiert

    }

    public void erzeugeSpielfeld() {
        createMap(11, 11);
    }

    public void createMap(int sizeX, int SizeY) {

        spielfeld = new Spielfeld(sizeX, SizeY);

        spielfeld.setFeld(0,0, Feldart.WAND);
        spielfeld.setFeld(0,0, Feldart.LEER);

        //Der Rand des Spielfeldes ist eine Wand
        for(int i=0; i<spielfeld.getHeight(); i++){
            spielfeld.setFeld(0,i, Feldart.WAND);
            spielfeld.setFeld(spielfeld.getWidth() - 1, i, Feldart.WAND);
        }
        for(int i=0; i<spielfeld.getWidth(); i++){
            spielfeld.setFeld(i, 0, Feldart.WAND);
            spielfeld.setFeld(i, spielfeld.getHeight() - 1, Feldart.WAND);
        }

        // Die Karte ist mti einem Raster aus einzelnen Wandfeldern bedeckt.
        // Der Rest wird zufälling mit leeren Feldern und Steinen bestückt.
        for(int x=1; x<spielfeld.getHeight()-1; x++){
            for(int y=1; y<spielfeld.getWidth()-1; y++){
                if(x%2==0 &&  y%2==0) {
                    spielfeld.setFeld(x,y, Feldart.WAND);
                } else if (Math.random() > 0.2) {
                    spielfeld.setFeld(x,y, Feldart.STEIN);
                } else {
                    spielfeld.setFeld(x,y, Feldart.LEER);
                }
            }
        }

    }


    public void placePlayersOnTheMap() {

        //Platziere Spieler
        for(Spieler playerToBePlaced: spieler) {
            boolean playerPositionFound = false;
            int counter = 0;
            while (!playerPositionFound ) {
                int posX = (int) Math.round(Math.random() * (spielfeld.getWidth()-2));
                int posY = (int) Math.round(Math.random() * (spielfeld.getHeight()-2));
                if(!existPlayerOnStartupPosition(posX, posY) && isPlayerPlacementPossible(posX, posY)) {
                    makePlayerLocationWalkable(posX, posY);
                    playerToBePlaced.setPosition(new Position(posX, posY));
                    playerPositionFound = true;
                    counter = 0;
                }
                counter ++;
                if(counter > 30){
                    throw new RuntimeException("Spieler konnten nicht positioniert werden");
                }
            }

        }

    }

    public boolean existPlayerOnStartupPosition(int posX, int posY){
        for(Spieler player: spieler) {
            if((player.getPosition() != null)
                    && (posX == player.getPosition().getX())
                    && (posY == player.getPosition().getY())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerPlacementPossible(int posX, int posY){
        int REQUIRED_ACCESSIBLE_FIELDS = 3;

        int counter = 0;
        counter += (spielfeld.getFeldArt(posX, posY).isPositionForPlayerPlacement())? 1 :0;
        counter += (spielfeld.getFeldArt(posX+1, posY).isPositionForPlayerPlacement())? 1 :0;
        counter += (spielfeld.getFeldArt(posX, posY+1).isPositionForPlayerPlacement())? 1 :0;
        counter += (spielfeld.getFeldArt(posX+1, posY+1).isPositionForPlayerPlacement())? 1 :0;

        return (counter >= REQUIRED_ACCESSIBLE_FIELDS);
    }

    public void makePlayerLocationWalkable(int posX, int posY){
        spielfeld.makeFieldAccessibleForPlayerPlacement(posX, posY);
        spielfeld.makeFieldAccessibleForPlayerPlacement(posX+1, posY);
        spielfeld.makeFieldAccessibleForPlayerPlacement(posX, posY+1);
        spielfeld.makeFieldAccessibleForPlayerPlacement(posX+1, posY+1);
    }

    public Spielfeld getSpielfeld() {
        return spielfeld;
    }
}

