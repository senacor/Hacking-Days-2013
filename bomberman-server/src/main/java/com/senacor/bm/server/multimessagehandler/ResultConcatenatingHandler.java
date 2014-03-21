package com.senacor.bm.server.multimessagehandler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public abstract class ResultConcatenatingHandler implements Handler<HttpServerRequest> {
    
    @Override
    public void handle(HttpServerRequest request) {
        request.response().setChunked(true).end(concatenateResults());
    }

    protected abstract String concatenateResults();

}
