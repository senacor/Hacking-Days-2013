package com.senacor.bm.server.multimessagehandler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

public class ResultKeepingHandler<E> implements Handler<Message<E>> {

    private E result = null;
    private Handler<ResultKeepingHandler<?>> resultHandler;
    private boolean resultSet = false;

    public E getResultMessage() {
        return result;
    }

    @Override
    public void handle(Message<E> event) {
        result = event.body();
        resultSet = true;
        resultHandler.handle(this);
    }
    
    public void setResultHandler(Handler<ResultKeepingHandler<?>> handler) {
        this.resultHandler = handler;
    }
    
    public boolean hasResult() {
        return this.resultSet;
    }

}
