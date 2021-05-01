package org.example.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsumerVerticle extends AbstractVerticle {

    private String name;

    @Override
    public void start(Promise<Void> startFuture) {
        vertx.eventBus().consumer("test-channel", message -> {
            System.out.println(this.name + " is received: " + message.body());
        });
    }

    @Override
    public void stop(Promise<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }

}
