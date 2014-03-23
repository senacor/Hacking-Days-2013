package com.senacor.bomberman.gameworld.model;

import com.senacor.bomberman.gameworld.GameWorldVerticle;
import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by abremm on 22.03.14.
 */
public class SpielfeldTest {

    @Test
    public void testToJsonObject() {
        Spielfeld feld = new Spielfeld(10, 15);
        
        JsonObject jsonObject = feld.toJsonObject();
        assertEquals(10, jsonObject.getNumber("width"));
        assertEquals(15, jsonObject.getNumber("height"));
    }
}
