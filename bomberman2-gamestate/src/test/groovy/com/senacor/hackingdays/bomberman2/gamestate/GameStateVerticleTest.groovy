package com.senacor.hackingdays.bomberman2.gamestate

import org.junit.Test
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.testtools.TestVerticle
import static org.vertx.testtools.VertxAssert.*;

/**
 *
 * @author Jochen Mader
 */

class GameStateVerticleTest extends TestVerticle{
    @Test
    public void testStartGame() {
        container.deployVerticle("groovy:"+GameStarterVerticle.class.getName(), new Handler<AsyncResult<java.lang.String>>() {
            @Override
            void handle(AsyncResult<String> event) {
                JsonArray participants = new JsonArray()
                participants.add("nr1")
                participants.add("nr2")
                JsonObject msg = new JsonObject()
                vertx.eventBus().registerHandler("nr1.start", new Handler<Object>(){
                    @Override
                    void handle(Object event2) {
                        testComplete();
                    }
                })
                msg.putArray("participants", participants)
                vertx.eventBus().send("game.start", msg)
            }
        });
    }

    @Test
    public void testStartGameAndSendCommands() {
        container.deployVerticle("groovy:"+GameStarterVerticle.class.getName(), new Handler<AsyncResult<java.lang.String>>() {
            @Override
            void handle(AsyncResult<String> event) {
                JsonArray participants = new JsonArray()
                participants.add("nr1")
                participants.add("nr2")
                JsonObject msg = new JsonObject()

                vertx.eventBus().registerHandler("nr1.start", new Handler<Object>(){
                    @Override
                    void handle(Object event2) {
                        JsonObject jsonObject = new JsonObject()
                        jsonObject.putString("player","nr1")
                        jsonObject.putString("command","nr1")
                        GameStateVerticleTest.this.vertx.eventBus().send("game.1", jsonObject)
                    }
                })

                vertx.eventBus().registerHandler("nr2.start", new Handler<Object>(){
                    @Override
                    void handle(Object event2) {
                        JsonObject jsonObject = new JsonObject()
                        jsonObject.putString("player","nr2")
                        jsonObject.putString("command","nr2")
                        GameStateVerticleTest.this.vertx.eventBus().send("game.1", jsonObject)
                    }
                })

                vertx.eventBus().registerHandler("nr1.nextround", new Handler<Object>(){
                    @Override
                    void handle(Object event2) {
                        println "3"
                        testComplete();
                    }
                })
                msg.putArray("participants", participants)
                vertx.eventBus().send("game.start", msg)

            }
        });
    }
}
