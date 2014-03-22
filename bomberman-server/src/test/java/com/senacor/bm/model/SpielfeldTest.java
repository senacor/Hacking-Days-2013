package com.senacor.bm.model;

import com.senacor.bm.services.BombermanVerticle;
import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by abremm on 22.03.14.
 */
public class SpielfeldTest {

    @Test
    public void testToJsonObject() {
        BombermanVerticle bombermanVerticle = new BombermanVerticle();
        bombermanVerticle.erzeugeSpielfeld();
        Spielfeld feld = bombermanVerticle.getSpielfeld();
        
        JsonObject jsonObject = feld.toJsonObject();
        assertNotNull(jsonObject.getNumber("width"));
        assertNotNull(jsonObject.getNumber("height"));

        System.out.println(jsonObject.toString());
    }
}
