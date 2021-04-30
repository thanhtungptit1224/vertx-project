package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // Vert instance khong lam gi nhieu ngoai quan ly thread, bus,...
        // No tao cac thread(not deamon) xu ly cac message giao tiep giua cac verticle
        // Deploy async

        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(2);
        options.setEventLoopPoolSize(1);
        Vertx vertx = Vertx.vertx(options);

//        vertx.deployVerticle(new ConsumerVerticle("Consumer 1"), stringAsyncResult -> {
//            System.out.println("Deploy Success 1");
//        });
//
//        vertx.deployVerticle(new ConsumerVerticle("Consumer 2"), stringAsyncResult -> {
//            System.out.println("Deploy Success 2");
//        });
//
//        vertx.deployVerticle(new OtherConsumerVerticle("Consumer Other"), stringAsyncResult -> {
//            System.out.println("Deploy Success Other");
//        });
//
//
//        Thread.sleep(3000);
//        vertx.deployVerticle(new ProducerVerticle());
//
//        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool");
//        executor.executeBlocking(promise -> {
//            //  Call some blocking API that takes a significant amount of time to return
//            String result = "";
//            promise.complete(result);
//        }, res -> {
//            System.out.println("The result is: " + res.result());
//        });

        vertx.deployVerticle(new VertxHttpServer(), stringAsyncResult -> {
            if (stringAsyncResult.succeeded())
                System.out.println("Deploy Http Server Success: " + stringAsyncResult);
            else
                System.out.println("Deploy Http Server Fail: " + stringAsyncResult);
        });
    }
}
