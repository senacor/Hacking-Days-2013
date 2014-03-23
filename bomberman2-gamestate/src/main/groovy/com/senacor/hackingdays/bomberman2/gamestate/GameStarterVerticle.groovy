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
        container.logger.info("started GameStarterVerticle");

        vertx.eventBus.registerHandler("game.start", { message ->
            gameIdCounter++
            //println(LockStepVerticle.class.getCanonicalName())
            println message.body
            Verticle deployable = container.deployVerticle("groovy:" + LockStepVerticle.class.getCanonicalName(),
                    ["participants" : message.body["participants"], "gameId" : gameIdCounter], {doneHandler ->
                            message.body["participants"].each {vertx.eventBus.send(it.get("name")+".start", gameIdCounter)}
                        })
            gameIdToVerticleMap.put(gameIdCounter, deployable)
        });

        vertx.eventBus.registerHandler("game.stop", { message ->
            println "undeploying";
            container.undeployVerticle(gameIdToVerticleMap[message.body])
        });
    }
}
