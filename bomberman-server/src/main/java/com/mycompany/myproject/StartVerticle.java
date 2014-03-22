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
 * Initiale anzeige des Spielfeldes 
 */
public class StartVerticle extends Verticle {

  public void start() {

    final Logger logger = container.logger();

    vertx.eventBus().registerHandler("state.init", new Handler<Message<String>>() {
      @Override
      public void handle(final Message<String> ignored) {
        // Hier die Logik für die Initiale Anzeige des Spielstandes
        ConcurrentSharedMap map =vertx.sharedData().getMap("gameMap");
        map.put("x", 0);
        map.put("y", 0);
        //...
        JsonObject msg = new JsonObject(map);
        vertx.eventBus().publish("state", msg);
        logger.info("Sent initial state: " + String.valueOf(msg));
      }
    });

    logger.info("StartVerticle started");

  }
}
