package com.senacor.bomberman.gameworld.services;

import com.senacor.bomberman.gameworld.GameWorldVerticle;
import com.senacor.bomberman.gameworld.model.Feldart;
import com.senacor.bomberman.gameworld.model.Gameworld;
import com.senacor.bomberman.gameworld.model.Spielfeld;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by abremm on 21.03.14.
 */
public class GameworldTest {

    @Test
    public void createSmalSpielfeld() {

        Gameworld csv = new Gameworld();
        Spielfeld spielfeld = csv.getSpielfeld();

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

        Gameworld csv = new Gameworld();
        Spielfeld spielfeld = csv.getSpielfeld();

        for(int i=0; i<spielfeld.getHeight(); i++){
            for(int y=0; y<spielfeld.getWidth(); y++){
                assertNotNull(spielfeld.getFeld(i,y));
            }
        }
    }
}
