package com.senacor.bm.server;

import com.senacor.bm.services.BombermanVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class StateVerticle extends Verticle {
	
	private String playerName = "unknown";
	private int points = 0;
	private boolean alive = true;
	
	public class SetPlayerNameHandler implements Handler<Message<String>> {
		@Override
		public void handle(Message<String> message) {
		    final String body = message.body();
		    if ((body != null) && (!"".equals(body))) {
				playerName = message.body();
			}
			message.reply(playerName);
		}
	}
	
	public class SetPointsHandler implements Handler<Message<Integer>> {
		@Override
		public void handle(Message<Integer> message) {
			if (message.body() != null) {
				points = message.body();
			}
			message.reply(points);
		}
	}
	
	public class SetAliveHandler implements Handler<Message<Boolean>> {
		@Override
		public void handle(Message<Boolean> message) {
			if (message.body() != null) {
				alive = message.body();
			}
			message.reply(alive);
		}
	}
	
	public class GetStatusHandler implements Handler<Message<Long>> {
		@Override
		public void handle(Message<Long> message) {
			String result = playerName + ": " + points + "(" + alive + ")";
			message.reply(result);
		} 
	}
	
	public static final String ADDR_PLAYERNAME = "com.senacor.playername";
	public static final String ADDR_ALIVE = "com.senacor.alive";
	public static final String ADDR_POINTS = "com.senacor.points";
	public static final String ADDR_STATUS = "com.senacor.status";
	
	@Override
	public void start() {
		vertx.eventBus().registerHandler(ADDR_PLAYERNAME, new SetPlayerNameHandler());
		vertx.eventBus().registerHandler(ADDR_ALIVE, new SetAliveHandler());
		vertx.eventBus().registerHandler(ADDR_POINTS, new SetPointsHandler());
		vertx.eventBus().registerHandler(ADDR_STATUS, new GetStatusHandler());

        container.deployVerticle(BombermanVerticle.class.getName());
	}

}
