/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 *
 */
package com.mycompany.myproject;


import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.shareddata.ConcurrentSharedMap;
import org.vertx.java.platform.Verticle;

/**
 * Tut nichts anderes als den aktuellen Spielstatus für den Client auf den 
 * Bus zu legen
 * 
 */
public class StateVerticle extends Verticle {

  public void start() {

    final Logger logger = container.logger();

    vertx.eventBus().registerHandler("state.update", new Handler<Message<String>>() {
      @Override
      public void handle(final Message<String> move) {
        // Hier die Auswertungslogik für den Spielstand einbauen
        ConcurrentSharedMap map =vertx.sharedData().getMap("gameMap");
        switch (move.body().charAt(0)) {
            case 'u':  map.put("y", ((int) map.get("y")) + 1); break;
            case 'd':  map.put("y", ((int) map.get("y")) - 1); break;
            case 'l':  map.put("x", ((int) map.get("x")) - 1); break;
            case 'r':  map.put("x", ((int) map.get("x")) + 1); break;
            default :
                throw new AssertionError("Seltsame bewegung" + move.body());
        }
        //...
        vertx.eventBus().publish("state", new JsonObject(map));
        logger.info("Sent updated state" + map);
      }
    });

    logger.info("StateVerticle started");

  }
}
