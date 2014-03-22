package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.platform.Verticle

/**
 *
 * @author Jochen Mader
 */
class GameStarterVerticle extends Verticle{

    def gameIdToVerticleMap = [:];
    Integer gameIdCounter = 0;

    @Override
    Object start() {
        vertx.eventBus.registerHandler("game.start", { message ->
            gameIdCounter++
            //println(LockStepVerticle.class.getCanonicalName())
            println message.body
            def deployable = container.deployVerticle("groovy:LockStepVerticle",
                    ["participants" : message.body["participants"], "gameId" : gameIdCounter], {doneHandler ->
                            gameIdToVerticleMap.put(gameIdCounter, deployable)
                            message.body["participants"].each {vertx.eventBus.send(it+".start", gameIdCounter)}
                        })
        });

        vertx.eventBus.registerHandler("game.stop", { message ->
            container.undeployVerticle(gameIdToVerticleMap[message.body])
        });
    }
}
