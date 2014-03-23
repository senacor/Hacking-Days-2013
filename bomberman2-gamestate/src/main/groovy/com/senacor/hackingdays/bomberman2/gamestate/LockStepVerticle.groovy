package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject

/**
 *
 * @author Jochen Mader
 */
class LockStepVerticle extends Verticle{
    @Override
    Object start() {

        def playNameToCommand = [:]
        def gameId = container.config["gameId"]
        def roundCounter = 1
        def participants = container.config["participants"]
        def captureState = [:]

        container.logger.info("started LockStepVerticle for game "+gameId);

        vertx.eventBus.registerHandler("game."+gameId, { message ->
            playNameToCommand.put(message.body["player"], message.body["command"])

            captureState = [gameId : gameId,
                    player: message.body["player"],
                    command: message.body["command"],
                    roundCounter: roundCounter];
            println ("gameId:" + gameId)
            println ("player:" + message.body["player"])
            println ("command:" + message.body["command"])

            println ("players:" +participants.size())
            println ("recived:" + playNameToCommand.size())

            if(playNameToCommand.size()==participants.size()){
                vertx.eventBus.send("game.capture.state", captureState)
                roundCounter++
                JsonObject nextroundMessage = new JsonObject()

                JsonArray updates = new JsonArray();
                for(String playerName : playNameToCommand.keySet()){
                    String direction = playNameToCommand.get(playerName);
                    JsonObject update = new JsonObject();
                    update.putString("player",playerName);
                    update.putString("direction",direction);
                    updates.add(update);
                }
                nextroundMessage.putArray("commands", updates)
                nextroundMessage.putNumber("roundid", roundCounter)
                vertx.eventBus.send("game.update", nextroundMessage);
            }
        })




        vertx.eventBus.registerHandler("game.update.reply", { message ->
            container.logger.info("handle game.update.reply");
            container.logger.info(message.body)
            JsonObject update = new JsonObject();
            update.putNumber("roundcounter", roundCounter);
            update.putArray("update", new JsonArray(message.body["update"]));
            participants.each {vertx.eventBus.send(it.get("name")+".nextround", update)}
            playNameToCommand.clear()
        })


        vertx.eventBus.registerHandler("game."+gameId+".join", { message ->
             def player =  message.body["player"];
             participants.add(player);

            vertx.eventBus.send(player.get("name")+".start", gameId)

            container.logger.info(player.get("name")+" joined game "+gameId);
            println ("players:" +participants.size())
            println ("recived:" + playNameToCommand.size())
        })
    }
}
