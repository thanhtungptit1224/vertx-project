package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Book;
import org.example.request.CreateBookRequest;
import org.example.service.BookService;

import static io.vertx.core.http.HttpMethod.POST;

@Slf4j
public class VertxHttpServer extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startFuture) {
        Router router = Router.router(vertx);

        router.route("/assets/*").handler(StaticHandler.create("assets")); // Serve static resources from the /assets directory
        router.route().handler(bodyHandler()); // The usage of this handler requires that it is installed as soon as possible in the router since it needs to install handlers to consume the HTTP request body and this must be done before executing any async call.

        router.route("/").handler(routingContext -> {
            routingContext.response().putHeader("content-type", "text/plain").end("Hello World!");
        });

        Route route = router.route("/multi-handler/");

        route.handler(routingContext -> {
            routingContext.response().putHeader("cac", "aca").setChunked(true).write("Start multiple handler\n");
            routingContext.next();
        });

        //Can't set header here
        route.handler(routingContext -> {
            routingContext.response().write("End of multiple handler");
            routingContext.end();
        });


        router.get("/api/token").handler(ctx -> {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end(getToken());
        });

//        router.route("/api/*").handler(CookieHandler.create());
//        router.route("/api/*").handler(SessionHandler.create(LocalSessionStore.create(vertx)));
//        router.route("/api/*").handler(UserSessionHandler.create(authProvider()));

        router.route("/api/*").handler(JWTAuthHandler.create(jwtAuthProvider()));
//        router.route("/api/*").handler(RedirectAuthHandler.create(authProvider(), "/assets/login.html"));
//        router.post("/login").handler(FormLoginHandler.create(authProvider()));

        router.post("/api/wine").handler(this::handlerPost);
        router.get("/api/wine/:id").handler(routingContext -> {routingContext.response().end("Get Wine. Pass Jwt");});
        router.put("/api/wine/:id").handler(null);
        router.delete("/api/wine/:id").handler(null);

        router.post("/api/book").handler(ctx -> {
            CreateBookRequest request = ctx.getBodyAsJson().mapTo(CreateBookRequest.class);
            Book book = new Book();
            book.setName(request.getName());
            book.setAuthor(request.getAuthor());
            book.setCategory(request.getCategory());

//            BookService bookService = BookServiceImpl.getInstance();
            BookService bookService = FactoryService.getInstance(BookService.class);
            bookService.create(book).onSuccess(book1 -> ctx.response().end(book1.toString()));
        });

        router.get("/logout").handler(context -> {
            context.clearUser();
            context.response().putHeader("location", "/assets/index.html").setStatusCode(302).end();
        });

        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });

        router.errorHandler(500, (ctx) -> ctx.response().setStatusCode(500).end("St Wrong"));
    }

    // jwt auth
    private JWTAuth jwtAuthProvider() {
//        JsonObject config = new JsonObject()
//                .put("keyStore", new JsonObject()
//                .put("type", "jceks")
//                .put("path", "keystore.jceks")
//                .put("password", "secret"));

        //Using symmetric key
        return JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("This is secret key")));

        //Using asymmetric key
//        return JWTAuth.create(vertx, new JWTAuthOptions()
//                .addPubSecKey(new PubSecKeyOptions()
//                        .setAlgorithm("RS256")
//                        .setBuffer("This is public key"))
//        .addPubSecKey(new PubSecKeyOptions().setAlgorithm("RSA256").setBuffer("This is private key")));
    }

    private String getToken() {
        JsonObject claims = new JsonObject().put("admin", "admin");
        JWTOptions jwtOptions = new JWTOptions();// For symmetric key
//        JWTOptions jwtOptions = new JWTOptions().setAlgorithm("RS256");// For asymmetric key
//        JWTOptions jwtOptions = new JWTOptions().setAlgorithm("ES256");// For ec key

        return jwtAuthProvider().generateToken(claims, jwtOptions);
    }

    private BodyHandler bodyHandler() {
        BodyHandler bodyHandler = BodyHandler.create();
        bodyHandler.setBodyLimit(2048); // byte
        bodyHandler.setMergeFormAttributes(false); // default true
        return bodyHandler;
    }

    private void handlerPost(RoutingContext routingContext) {
        JsonObject body = routingContext.getBodyAsJson();
        System.out.println("This is body: " + body);

//        Set<Cookie> cookie = routingContext.cookies();
//        System.out.println("This is cookie: " + cookie);
//
//        User user = routingContext.user(); // If authentication or authorization
//        System.out.println("This is user: " + user.principal());
//
//        routingContext.addCookie(Cookie.cookie("cookie-name", "cookie-value"));

        Session session = routingContext.session();
        System.out.println("This is session: " + session.id());

        routingContext.response().end("LOL");
    }

    private void exec(HttpServerRequest httpServerRequest) {
        if (httpServerRequest.method() == POST) {
            System.out.println("This is one");
            httpServerRequest.response().end("LOL");

            // The body handler is called whenever data from the request body arrives.
//            httpServerRequest.handler(buffer -> {
//                HttpServerResponse httpServerResponse = httpServerRequest.response();
//                httpServerResponse.headers().add("Content-Type", "text/plain");
//                httpServerResponse.headers().add("Content-Length", String.valueOf("This Body From Vertx 1".getBytes().length));
//                httpServerResponse.write("This Body From Vertx 1");
//                httpServerResponse.write("This Body From Vertx 2");
//                httpServerResponse.end("LOL");
//            });

            // The end handler is not called until the full HTTP POST body has been received.
            // However, the end handler does not have direct access to the full HTTP POST body.
            // You need to collect that in the request handler.
//            httpServerRequest.endHandler(aVoid -> {
//                HttpServerResponse httpServerResponse = httpServerRequest.response();
//                httpServerResponse.headers().add("Content-Type", "text/plain");
//                httpServerResponse.write("This Body From Vertx 1");
//                httpServerResponse.write("This Body From Vertx 2");
//                httpServerResponse.end();
//            });
        }
    }

}
