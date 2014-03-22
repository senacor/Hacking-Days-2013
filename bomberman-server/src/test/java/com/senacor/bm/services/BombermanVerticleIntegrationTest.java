package com.senacor.bm.services;/*
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
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;


public class BombermanVerticleIntegrationTest extends TestVerticle {

  @Test
  public void testEventErmittleSpielfeld() {

    container.logger().info("send event: game.map.full");
    vertx.eventBus().send("game.map.full", "???", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> reply) {
          JsonObject map = reply.body();
          container.logger().info("received jsonObject: " + map.toString());
          assertNotNull(map.getNumber("width"));
          testComplete();
      }
    });
  }

  @Override
  public void start() {
    // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
    initialize();
    // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
    // don't have to hardecode it in your tests
    container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
      @Override
      public void handle(AsyncResult<String> asyncResult) {
      // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
      if (asyncResult.failed()) {
        container.logger().error(asyncResult.cause());
      }
      assertTrue(asyncResult.succeeded());
      assertNotNull("deploymentID should not be null", asyncResult.result());
      // If deployed correctly then start the tests!
      startTests();
      }
    });
  }

}
