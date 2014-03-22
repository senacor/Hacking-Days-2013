package com.senacor.bomberman.gameworld.model;

import com.senacor.bomberman.gameworld.GameWorldVerticle;
import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by abremm on 22.03.14.
 */
public class SpielfeldTest {

    @Test
    public void testToJsonObject() {
        GameWorldVerticle bombermanVerticle = new GameWorldVerticle();
        bombermanVerticle.erzeugeSpielfeld();
        Spielfeld feld = bombermanVerticle.getSpielfeld();
        
        JsonObject jsonObject = feld.toJsonObject();
        assertNotNull(jsonObject.getNumber("width"));
        assertNotNull(jsonObject.getNumber("height"));

        System.out.println(jsonObject.toString());
    }
}
