package org.example;

import io.vertx.core.AbstractVerticle;

public class ProducerVerticle extends AbstractVerticle {
    @Override
    public void start() {
//        vertx.eventBus().publish("test-channel", "message publish"); // to multiple handler
//        vertx.eventBus().send   ("test-channel", "message send", messageAsyncResult -> {
//            if (messageAsyncResult.succeeded()) {
//                // Receive reply;
//            }
//        }); // if multiple handler -> round robin
    }
}
