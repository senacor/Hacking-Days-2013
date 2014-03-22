package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.platform.Verticle

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
        vertx.eventBus.registerHandler("game."+gameId, { message ->
            playNameToCommand.put(message.body["player"], message.body["command"])
            if(playNameToCommand.size()==participants.size()){
                roundCounter++
                playNameToCommand.clear()
                participants.each {vertx.eventBus.send(it+".nextround", roundCounter)}
            }
        })
    }
}
