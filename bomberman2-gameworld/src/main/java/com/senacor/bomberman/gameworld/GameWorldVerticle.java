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
 * 
 */
public class GameWorldVerticle extends Verticle {

    public static final String GAME_INIT = "game.initialize";
    public static final String GAME_JOIN = "game.join";
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

        vertx.eventBus().registerHandler(GAME_INIT, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("initializing game");

                clearGameData();

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

        vertx.eventBus().registerHandler(GAME_JOIN, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                container.logger().info("join game");

                if(spielfeld == null){
                    throw new RuntimeException("Es gibt noch kein Spiel!");
                }

                String name = message.body().getString("Name");
                int gameId = message.body().getInteger("GameId");

                Spieler player = new Spieler(name.toString());
                spieler.add(0, player);
                placePlayerOnTheMap(player);

                // reply initial map
                message.reply(getGameWorldJsonObject());

                // update game state.
                JsonObject joinMsg = new JsonObject();
                joinMsg.putObject("player", player.toJsonObject());
                vertx.eventBus().send("game."+gameId+".join", joinMsg);

                container.logger().info("game joined");
            }
        });

        vertx.eventBus().registerHandler(GAME_UPDATE, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                //Zeitabschnitt aus und Prüfe
                int currentTimeSlice=0;

                try {
                    if(message.body().getInteger("currentTimeSlice")!=null){
                        currentTimeSlice = message.body().getInteger("currentTimeSlice");
                    }
                } catch(NullPointerException e){
                    message.reply();
                }

                //Bewegungen abshließen
                //für alle bewegenden Nutzer, die fällig sind:
                for(Spieler player: spieler) {
                    if(player.isMoving()) {
                        pruefePositionswechselNutzer(player, currentTimeSlice);
                        pruefeBewegungNutzerAbgeschlossen(player, currentTimeSlice);
                    }
                }


                JsonArray playerMovements = message.body().getArray("PlayerMovements");

                //für alle Bewegungsupdates
                for(Object playerMovement: playerMovements) {
                    String playerName = ((JsonObject)playerMovement).getString("Player");
                    String direction = ((JsonObject)playerMovement).getString("Direction");
                    Spieler player = findPlayerByName(playerName);
                    if(player != null) {
                        bewegungSpieler(player, direction);
                    }
                }

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

    private Spieler findPlayerByName(String playerName) {
        for (Spieler player: spieler){
            if(player.getPlayerName().equals(playerName)){
                return player;
            }
        }
        return null;
    }

    void clearGameData() {
        spieler.clear();
        platzierteBomben.clear();
        platzierteItem.clear();
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
            spieler.add(new Spieler("Bomberman"));
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

    public void bewegungSpieler(Spieler player, String direction) {
        // Stelle sicher, dass sich der Spieler nicht bewegt
        if(!player.isMoving()) {

            Position newPosition = new Position(player.getPosition());
            newPosition.move(direction);

            if ((newPosition.getX() < 0) || newPosition.getX() >= spielfeld.getWidth() ||
                    (newPosition.getY() < 0) || newPosition.getY() >= spielfeld.getHeight()) {
                return;
            }

            // Prüfe ob, Zielposition frei ist (keine Wand und Bombe)
            if (!spielfeld.isFieldAccessible(newPosition)) {
                return;
            }

            // Timer  einstellen (halbe Bewegungszeit): Positionswechsel
            player.setTimeSliceReachingNextField(1);
            // Timer einstellen (volle Bewegungszeit): Bewegung abgeschlossen
            player.setTimeSliceFinishingMovement(1);
            // Event Positionswechsel
            player.setTargetPosition(newPosition);
        }
    }

    public void pruefePositionswechselNutzer(Spieler player, int currentTimeSlice) {
        if(player.getTimeSliceReachingNextField() == currentTimeSlice) {
            player.reachTargetField();

            // Prüfe Feld auf Item
            //      - Auswertung des Items und Aktualisierung der Nutzerattribute
            //      - Broadcast  „Item entfernen“
        }

    }

    public void pruefeBewegungNutzerAbgeschlossen(Spieler player, int currentTimeSlice) {
        if (player.getTimeSliceFinishingMovement() == currentTimeSlice) {
            player.finishMovement();
        }
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
            placePlayerOnTheMap(playerToBePlaced);
        }
    }

    private void placePlayerOnTheMap(Spieler playerToBePlaced) {
        boolean playerPositionFound = false;
        int counter = 0;
        while (!playerPositionFound ) {
            int posX = (int) Math.round(Math.random() * (spielfeld.getWidth()-2));
            int posY = (int) Math.round(Math.random() * (spielfeld.getHeight()-2));
            if(!existPlayerOnStartupPosition(posX, posY) && isFieldAccessable(posX, posY) && isPlayerPlacementPossible(posX, posY)) {
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

    public boolean isFieldAccessable(int posX, int posY) {
        return spielfeld.getFeldArt(posX, posY).isAccessible();
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

