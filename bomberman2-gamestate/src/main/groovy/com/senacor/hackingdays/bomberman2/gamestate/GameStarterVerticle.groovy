package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.platform.Verticle

/**
 *
 * @author Jochen Mader
 */
class GameStarterVerticle extends Verticle{

    def gameIdToVerticleMap = [:];

    @Override
    Object start() {
        // game.start -
        vertx.eventBus.registerHandler("game.start", { message ->
            container.logger.info("HANDLER: game.start");

            final Integer gameId = message.body["gameId"];

            println message.body
            Verticle deployable = container.deployVerticle("groovy:src/main/groovy/com/senacor/hackingdays/bomberman2/gamestate/LockStepVerticle",
                    ["participants" : message.body["participants"], "gameId" : gameId], {doneHandler ->
                            message.body["participants"].each {
                                vertx.eventBus.send(it.get("name")+".start", gameId)
                            }
                            vertx.eventBus.send("dashboard.start", gameId)
                        })
            gameIdToVerticleMap.put(gameId, deployable)
        });

        vertx.eventBus.registerHandler("game.stop", { message ->
            println "undeploying";
            container.undeployVerticle(gameIdToVerticleMap[message.body])
        });
    }
}
