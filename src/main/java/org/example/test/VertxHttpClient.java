package org.example.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;

public class VertxHttpClient extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startFuture) {
        HttpClient httpClient = vertx.createHttpClient();

        // Async to avoid blocking the event loop.
        // Java's built-in URL and URLConnection are not asynchronous, so you should not use them.
//        httpClient.getNow(9090, "domain", "/path", httpClientResponse -> {
//            // If you don't need to handle body of response
//            // ...
//            // Of if you need to handle body of response. So...
//            httpClientResponse.bodyHandler(buffer -> {
//                System.out.println("Response (" + buffer.length() + "): ");
//                System.out.println(buffer.getString(0, buffer.length()));
//            });
//        });
    }
}
