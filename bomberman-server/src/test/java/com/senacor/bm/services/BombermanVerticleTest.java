package com.senacor.bm.services;

import com.senacor.bm.model.Feldart;
import com.senacor.bm.model.Spielfeld;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by abremm on 21.03.14.
 */
public class BombermanVerticleTest {

    @Test
    public void createSmalSpielfeld() {

        BombermanVerticle csv = new BombermanVerticle();
        Spielfeld spielfeld = csv.erzeugeSpielfeld();

        for(int i=0; i<spielfeld.getHeight(); i++){
            assertEquals("W", spielfeld.getFeld(0, i));
            assertEquals("W", spielfeld.getFeld(spielfeld.getWidth()-1, i));
        }

        for(int i=0; i<spielfeld.getWidth(); i++){
            assertEquals("W", spielfeld.getFeld(i, 0));
            assertEquals("W", spielfeld.getFeld(i, spielfeld.getHeight()-1));
        }
    }

    @Test
    public void spielfeldIstVollstaendig() {
        BombermanVerticle csv = new BombermanVerticle();
        Spielfeld spielfeld = csv.erzeugeSpielfeld();

        for(int i=0; i<spielfeld.getHeight(); i++){
            for(int y=0; y<spielfeld.getWidth(); y++){
                assertNotNull(spielfeld.getFeld(i,y));
            }
        }
    }
}
