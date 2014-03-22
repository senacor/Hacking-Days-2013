package com.senacor.bm.model;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.senacor.bm.services.CreateSpielfeldVerticle;
import org.junit.Test;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by abremm on 21.03.14.
 */
public class SpielfeldJSONTest {

    @Test
    public void test() throws IOException {

        CreateSpielfeldVerticle creator = new CreateSpielfeldVerticle();
        Spielfeld spielfeld = creator.createSpielfeld();

        Gson gson = new Gson();
        String json = gson.toJson(spielfeld);

        System.out.println(json);


        Spielfeld sp = gson.fromJson("{\"width\":11,\"height\":11,\"felder\":[[\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\"],[\"WAND\",\"STEIN\",\"STEIN\",\"LEER\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"WAND\"],[\"WAND\",\"LEER\",\"WAND\",\"LEER\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\"],[\"WAND\",\"LEER\",\"STEIN\",\"LEER\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"LEER\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"STEIN\",\"LEER\",\"STEIN\",\"STEIN\",\"STEIN\",\"LEER\",\"STEIN\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\",\"STEIN\",\"WAND\"],[\"WAND\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"STEIN\",\"LEER\",\"STEIN\",\"STEIN\",\"STEIN\",\"WAND\"],[\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\",\"WAND\"]]}", Spielfeld.class);
        assertEquals(11, sp.getWidth());
        assertEquals(11, sp.getHeight());
        assertEquals(Feldart.WAND, sp.getFeld(0,0));
        assertEquals(Feldart.STEIN, sp.getFeld(1,1));



        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        aClass.outputStreamMethod(os);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(spielfeld)); // where 'dst' can be File, OutputStream or Writer
//
//        String aString = new String(os.toByteArray(),"UTF-8");
//        System.out.println(aString);


        Feldart[][] a = new Feldart[2][2];
        a[0][0] = Feldart.WAND;
        a[0][1] = Feldart.WAND;
        a[1][0] = Feldart.LEER;
        a[1][1] = Feldart.LEER;

        JsonObject jo = new JsonObject();
        jo.putNumber("width", 11);
        jo.putNumber("heigth", 11);

        JsonArray ar =  new JsonArray(a);

        jo.putArray("felder", ar);

        System.out.println(jo.toString());
    }
}
