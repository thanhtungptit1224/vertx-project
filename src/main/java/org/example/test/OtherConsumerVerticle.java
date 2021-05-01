package org.example.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OtherConsumerVerticle extends AbstractVerticle {
    private String name;

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("test-channel", message -> {
            System.out.println(this.name + " is received: " + message.body());
            message.headers();
            message.reply(null);
        }).completionHandler(voidAsyncResult -> {
            // registered success
        });
    }
}
