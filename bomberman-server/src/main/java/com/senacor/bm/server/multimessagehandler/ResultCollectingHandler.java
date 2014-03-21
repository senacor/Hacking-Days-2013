package com.senacor.bm.server.multimessagehandler;

import java.util.HashSet;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class ResultCollectingHandler implements Handler<ResultKeepingHandler<?>> {
    
    private final Set<ResultKeepingHandler<?>> resultKeepingHandlers = new HashSet<>();  
    private final ResultConcatenatingHandler concatenator;
    private final HttpServerRequest request;
    
    public ResultCollectingHandler(ResultConcatenatingHandler concatenator, HttpServerRequest request) {
        this.concatenator = concatenator;
        this.request = request;
    }
    
    public void addResultKeepingHandlers(ResultKeepingHandler<?> ... handlers) {
        for (ResultKeepingHandler<?> handler: handlers) {
            this.resultKeepingHandlers.add(handler);
            handler.setResultHandler(this);
        }
    }
    
    @Override
    public void handle(ResultKeepingHandler<?> eventHandler) {
        if (!resultKeepingHandlers.remove(eventHandler)) {
            throw new RuntimeException("EventHandler Unknown!");
        }
        if (resultKeepingHandlers.isEmpty()) {
            concatenator.handle(request);
        }
    }
}
