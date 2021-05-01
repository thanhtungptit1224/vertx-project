package org.example.test;

public class Docs {
    /**
     * FAQ:
     * Event Lopezes là một thread nhưng có exec code không?
     * Deploy nhiều instance của vertices khi nào?
     * Deploy verticles HA khi nao?
     * */

    /**
     * Vertx instance là trái tim của Vertx: Vertx vertx = Vertx.vertx();
     * Most applications will only need a single Vert.x instance, but it’s possible to create multiple Vert.x instances
     * Vert instance just manage thread, bus,... Create thread(not daemon) to handle message between verticle.
     * Block code can be executed by two ways. Vertx instance and worker verticle(Inside worker pool).
     * Nếu tạo clustered Vertx, thường sử dụng biến thể không đồng bộ để tạo vertx object, vì cần mất một khoảng thời gian
     * để các vertx instance đươc nhóm lại cùng với nhau. Trơng thời gian này, chúng ta không muốn block calling thread.
     * Don’t call us, we’ll call you: Vert.x will call you by sending you events. You handle events by providing handlers to the Vert.x APIs
     * In most cases Vert.x calls your handlers using a thread called an event loop.
     * -> Reactor Pattern: có một event loop chuyển giao các event đến các handler(Nodejs)
     * Không giống Nodejs, một Vertx instance có thể duy trì nhiều event loop -> Multi Reactor Pattern.
     * (Don't worry, cùng một handler sẽ chỉ được thực thi trên cùng một event loop -> không lo về concurrency)
     * Vertx sẽ log nếu có event loop bị block: Thread vertx-eventloop-thread-3 has been blocked for 20458 ms
     * Verticles được deploy và run bởi Vertx instance với core*2 event loop by default.
     * Verticles(Actor model) giao tiếp với nhau bằng cách send message qua event bus: Standard, Worker
     * Standard Verticles: using event loop thread, call code is executed on the same event loop.
     * Worker Verticles: using thread from worker pool, never executed concurrently by more than one thread, but can executed by different threads at different times.
     * There is a single event bus instance for every Vert.x instance. The event bus supports publish/subscribe, point-to-point, and request-response messaging.
     * -> all events -> event bus -> handlers -> event loop -> call stack
     *
     * Neu response tu nhieu handler, setChunk(true) tren handler dau tien
     * Blocking handler thuc thi tren cung context va co thu tu, neu khong quan tam thu tu co the thuc hien parallel
     * Neu dung multipart form, phai dung non-blocking-handler, ctx.request().setExpectMultipart(true);
     * */
}
