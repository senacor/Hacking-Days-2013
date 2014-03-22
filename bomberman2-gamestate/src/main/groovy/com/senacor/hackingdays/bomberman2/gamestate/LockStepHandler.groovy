package com.senacor.hackingdays.bomberman2.gamestate

import org.vertx.groovy.core.eventbus.Message
import org.vertx.java.core.json.JsonArray

/**
 *
 * @author Jochen Mader
 */
class LockStepHandler {

    JsonArray participants;
    def playNameToCommand = [:]
    def roundCounter = 1
    def closure


    LockStepHandler(JsonArray participants, Closure<?> onStepHandler) {
        this.participants = participants
        this.closure = onStepHandler
    }

    void receive(Message message) {
        playNameToCommand.put(message.body["player"], message.body["command"])
        if(playNameToCommand.size()==participants.size()){
            roundCounter++
            closure(roundCounter)
        }
    }
}
