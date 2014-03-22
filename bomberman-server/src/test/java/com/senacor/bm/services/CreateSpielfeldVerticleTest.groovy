package com.senacor.bm.services

import com.senacor.bm.model.Feldart
import com.senacor.bm.model.Spielfeld
import org.junit.Test

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertNotNull

/**
 * Created by abremm on 21.03.14.
 */
class CreateSpielfeldVerticleTest {

    @Test
    public void createSmalSpielfeld() {

        CreateSpielfeldVerticle csv = new CreateSpielfeldVerticle();
        Spielfeld spielfeld = csv.createSpielfeld();

        for(int i=0; i<spielfeld.getHeight(); i++){
            assertEquals(Feldart.WAND, spielfeld.getFeld(0, i));
            assertEquals(Feldart.WAND, spielfeld.getFeld(spielfeld.width-1, i));
        }

        for(int i=0; i<spielfeld.getWidth(); i++){
            assertEquals(Feldart.WAND, spielfeld.getFeld(i, 0));
            assertEquals(Feldart.WAND, spielfeld.getFeld(i, spielfeld.width-1));
        }
    }

    @Test
    public void spielfeldIstVollstaendig() {
        CreateSpielfeldVerticle csv = new CreateSpielfeldVerticle();
        Spielfeld spielfeld = csv.createSpielfeld();

        for(int i=0; i<spielfeld.getHeight(); i++){
            for(int y=0; y<spielfeld.getWidth(); y++){
                assertNotNull(spielfeld.getFeld(i,y));
            }
        }

    }
}
