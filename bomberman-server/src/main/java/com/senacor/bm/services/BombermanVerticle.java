package com.senacor.bm.services;

import com.senacor.bm.model.Feldart;
import com.senacor.bm.model.Spiel;
import com.senacor.bm.model.Spielfeld;
import org.vertx.java.platform.Verticle;

/**
 * Created by abremm on 21.03.14.
 */
public class BombermanVerticle extends Verticle {

    public Spielfeld spielfeld;

    public void start() {

        // Initialisieren des Spielfeldes
        spielfeld = createSpielfeld();

    }

    public Spielfeld createSpielfeld() {

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
