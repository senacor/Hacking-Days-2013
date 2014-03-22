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
            gameIdToVerticleMap.put(gameIdCounter, container.deployVerticle("groovy:"+LockStepVerticle.class.getCanonicalName(),
                    ["participants" : message.body["participants"], "gameId" : gameIdCounter]))
            message.body["participants"].each {vertx.eventBus.send(it+".start", gameIdCounter)}
        });

        vertx.eventBus.registerHandler("game.stop", { message ->
            container.undeployVerticle(gameIdToVerticleMap[message.body])
        });
    }
}
