package com.senacor.bm.services;

import com.senacor.bm.model.*;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.*;

/**
 * Created by abremm, mmenzel on 21.03.14.
 */
public class BombermanVerticle extends Verticle {

    private Spielfeld spielfeld;
    private List<Spieler> spieler;
    private List<PlacedBomb> platzierteBomben;
    private List<PlacedItem> platzierteItem;

    public void start() {

        // Initialisieren des Spielfeldes
        spielfeld = erzeugeSpielfeld();
        spieler = new LinkedList<Spieler>();
        platzierteBomben = new LinkedList<PlacedBomb>();
        platzierteItem = new LinkedList<PlacedItem>();

        Spieler player = new Spieler("hans");
        player.setPosition(new Position(2,2));
        spieler.add(player);

        Spieler player2 = new Spieler("susi");
        player2.setPosition(new Position(10,10));
        spieler.add(player2);

        vertx.eventBus().registerHandler("game.map.full", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {

                JsonObject fullGameWorld = new JsonObject();
                fullGameWorld.putArray("player", new JsonArray((List)spieler));
                fullGameWorld.putArray("bombs", new JsonArray((List)platzierteBomben));
                fullGameWorld.putArray("items", new JsonArray((List)platzierteItem));
                fullGameWorld.putObject("map", spielfeld.toJsonObject());
                message.reply(fullGameWorld);
            }
        });

        //Prüfung, ob Name schon vergeben ist und Anlage eines neuen Spielers
        vertx.eventBus().registerHandler("RegistriereSpieler", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                String nutzername = "";
                // 1) Prüfe: gibt es schon einen Nutzer mit dem Benutzernamen

                // 2) Spieler anlegen

                spieler.add(new Spieler(nutzername));

                PlatziereSpieler();
                //???
                message.reply("OK");

            }
        });

        vertx.eventBus().registerHandler("Spieleraktionen", new Handler<Message<JsonObject>>() {
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

    public void PlatziereSpieler() {

        //Platziere Spieler


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

    public Spielfeld erzeugeSpielfeld() {

        Spielfeld spielfeld = new Spielfeld(11, 11);

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
        return spielfeld;
    }
}
