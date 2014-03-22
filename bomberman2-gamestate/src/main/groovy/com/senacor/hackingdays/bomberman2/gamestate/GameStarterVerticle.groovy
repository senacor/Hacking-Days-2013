package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.json.JsonArray

/**
 *
 * @author Jochen Mader
 */
class GameStarterVerticle extends Verticle{

    def gameIdToClosureMap = [:];
    Integer gameIdCounter = 0;

    @Override
    Object start() {
        vertx.eventBus.registerHandler("game.start", { message ->
            gameIdCounter++
            JsonArray participants = message.body["participants"]
            LockStepHandler lockStepHandler = new LockStepHandler(participants, { roundCounter ->
                participants.each {vertx.eventBus.send(it+".nextround", roundCounter)}
            });
            Closure handler = { incoming ->
                lockStepHandler.receive(incoming)
            }
            vertx.eventBus.registerHandler("game."+gameIdCounter, handler)

            gameIdToClosureMap.put(gameIdCounter, handler)
            participants.each {vertx.eventBus.send(it+".start", gameIdCounter)}
        });

        vertx.eventBus.registerHandler("game.stop", { message ->
            vertx.eventBus.unregisterHandler("game."+message.body, gameIdToClosureMap.remove(gameIdCounter))
        });
    }

}
