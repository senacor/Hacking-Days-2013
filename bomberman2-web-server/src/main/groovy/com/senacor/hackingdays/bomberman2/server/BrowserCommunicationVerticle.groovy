package com.senacor.hackingdays.bomberman2.server

import org.vertx.groovy.core.buffer.Buffer
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.net.NetSocket
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.shareddata.ConcurrentSharedMap

/**
 *
 * @author Jochen Mader
 */
class BrowserCommunicationVerticle extends Verticle{

    @Override
    Object start() {

        vertx.createNetServer().connectHandler { NetSocket socket ->
            socket.dataHandler {Buffer buffer ->
                println "I received ${buffer.getString(0, buffer.length)}"
            }
            socket.write("hello\n");
        }.listen(2000,"localhost")

        vertx.eventBus.registerHandler("web.fullmap") { Message message ->
            vertx.eventBus.send("template.render", "template.mustache", { message2 ->
                message.reply(message2.body)
            })
        };
    }


    private JsonArray mapToArray(Map<Integer, String> environmentMap) {
        String[] mapArray = new String[environmentMap.size()];
        for(i in 0..(environmentMap.size()-1)) {
            mapArray[i] = environmentMap.get(i)
        }
        return new JsonArray(mapArray)
    }
}
