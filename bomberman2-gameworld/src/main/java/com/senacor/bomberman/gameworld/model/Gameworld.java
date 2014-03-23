package com.senacor.bomberman.gameworld.model;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by abremm on 23.03.14.
 */
public class Gameworld {

    public static final int DEFAULT_MAP_WIDTH = 11;
    public static final int DEFAULT_MAP_HEIGTH = 11;

    private int gameId;
    private Spielfeld spielfeld;
    private List<Spieler> spieler = new LinkedList<Spieler>();
    private List<PlacedBomb> platzierteBomben = new LinkedList<PlacedBomb>();
    private List<PlacedItem> platzierteItem = new LinkedList<PlacedItem>();

    private int currentTimeSlice=0;

    public Gameworld() {
        this(1, DEFAULT_MAP_WIDTH, DEFAULT_MAP_HEIGTH, null);
    }

    public Gameworld(int gameId, Integer mapWidth, Integer mapHeigth, JsonArray playerArray) {
        this.gameId = gameId;
        createPlayerList(playerArray);
        createMap(mapWidth.intValue(), mapHeigth.intValue());
        placePlayersOnTheMap();
    }

    public Spieler enterGameWorld(String playerName) {
        if(!isNameUnique(playerName)) {
            throw new RuntimeException("Name bereits vorhanden.");
        }
        Spieler player = new Spieler(playerName);
        addPlayer(player);
        placePlayerOnTheMap(player);
        return player;
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

    public JsonObject update(JsonArray playerMovements) {
        currentTimeSlice++;
        //Zeitabschnitt aus und Prüfe
//                int currentTimeSlice=0;

        try {
//                    if(message.body().getInteger("currentTimeSlice")!=null){
//                        currentTimeSlice = message.body().getInteger("currentTimeSlice");
//                    }
        } catch(NullPointerException e){
            return null;
        }

        //Bewegungen abshließen
        //für alle bewegenden Nutzer, die fällig sind:
        for(Spieler player: spieler) {
            player.setDirection("");
            if(player.isMoving()) {
                pruefePositionswechselNutzer(player, currentTimeSlice);
                pruefeBewegungNutzerAbgeschlossen(player, currentTimeSlice);
            }
        }

        //für alle Bewegungsupdates
        for(Object playerMovement: playerMovements) {
            String playerName = ((JsonObject)playerMovement).getString("player");
            String direction = ((JsonObject)playerMovement).getString("direction");
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

        JsonObject update = new JsonObject();
        JsonArray playerUpdates = new JsonArray();
        for(Spieler s : spieler){
            playerUpdates.add(s.getUpdateJsonObject());
        }
        update.putArray("update", playerUpdates);
        return update;
    }


    private void addPlayer(Spieler player) {
        spieler.add(0, player);
    }

    private boolean isNameUnique(String name) {
        boolean nameIsUnique = false;
        for(Spieler s : spieler) {
            if(s.equals(name)){
                return false;
            }
        }
        return true;
    }

    private Spieler findPlayerByName(String playerName) {
        for (Spieler player: spieler){
            if(player.getPlayerName().equals(playerName)){
                return player;
            }
        }
        return null;
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

            player.setPosition(newPosition);
            // Timer  einstellen (halbe Bewegungszeit): Positionswechsel
            player.setTimeSliceReachingNextField(0);
            // Timer einstellen (volle Bewegungszeit): Bewegung abgeschlossen
            player.setTimeSliceFinishingMovement(0);
            // Event Positionswechsel
            player.setTargetPosition(newPosition);
            player.setDirection(direction);
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

    public JsonObject getGameWorldJsonObject() {
        JsonObject fullGameWorld = new JsonObject();
        fullGameWorld.putArray("player", getPlayer());
        fullGameWorld.putArray("bombs", getBombs());
        fullGameWorld.putArray("items", getItems());
        fullGameWorld.putObject("map", spielfeld.toJsonObject());
        return fullGameWorld;
    }

    public JsonArray getPlayer() {
        JsonArray result = new JsonArray();
        for(Spieler p : spieler){
            result.add(p.toJsonObject());
        }
        return result;
    }

    public JsonArray getItems() {
        JsonArray result = new JsonArray();
        for(PlacedItem i : platzierteItem){
            result.add(i.toJsonObject());
        }
        return result;
    }

    public JsonArray getBombs() {
        JsonArray result = new JsonArray();
        for(PlacedBomb b : platzierteBomben){
            result.add(b.toJsonObject());
        }
        return result;
    }

    public Spielfeld getSpielfeld() {
        return spielfeld;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
