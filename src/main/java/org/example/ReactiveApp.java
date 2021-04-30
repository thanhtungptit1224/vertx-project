//package org.example;
//
//import io.reactivex.Flowable;
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Function;
//import io.vertx.core.http.RequestOptions;
//import io.vertx.core.json.JsonObject;
//import io.vertx.reactivex.core.buffer.Buffer;
//import io.vertx.reactivex.core.http.HttpClient;
//import io.vertx.reactivex.core.http.HttpClientRequest;
//import io.vertx.reactivex.core.http.HttpClientResponse;
//import org.example.model.CityAndDayLength;
//import org.reactivestreams.Publisher;
//
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReactiveApp {
//
//    private static RequestOptions metawether = new RequestOptions()
//            .setHost("www.metaweather.com")
//            .setPort(443)
//            .setSsl(true);
//
//    public static void main(String[] args) {
//        Observer<Object> intSubscriber = new Observer<Object>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable disposable) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(Object integer) {
//                System.out.println("onNext: "+ integer);
//            }
//        };
//
//        List<String> lol = new ArrayList<>();
//        lol.add("LOL");
//        Observable<Object> justObservable = Observable.just(4,4,6,lol);
//        justObservable.subscribe(intSubscriber);
//    }
//
////    public static void main(String[] args) {
////        Vertx vertx = io.vertx.reactivex.core.Vertx.vertx();
////        FileSystem fileSystem = vertx.fileSystem();
////        HttpClient httpClient = vertx.createHttpClient();
////
////        fileSystem
////                .rxReadFile("/Users/nguyenthanhtung/Documents/Development/Mine/vertx-project/src/main/resources/cities.text").toFlowable()
////                .flatMap(buffer -> Flowable.fromArray(buffer.toString().split("\\r?\\n")))
////                .flatMap(city -> searchByCityName(httpClient, city))
////                .flatMap(HttpClientResponse::toFlowable)
////                .map(extractingWoeid())
////                .flatMap(cityId -> getDataByPlaceId(httpClient, cityId))
////                .flatMap(toBufferFlowable())
////                .map(Buffer::toJsonObject)
////                .map(toCityAndDayLength())
////                .subscribe(System.out::println, Throwable::printStackTrace);
////    }
//
//    private static Flowable<HttpClientResponse> searchByCityName(HttpClient httpClient, String cityName) {
//        HttpClientRequest req = httpClient.get(
//                new RequestOptions()
//                        .setHost("www.metaweather.com")
//                        .setPort(443)
//                        .setSsl(true)
//                        .setURI(String.format("/api/location/search/?query=%s", cityName)));
//        return req
//                .toFlowable()
//                .doOnSubscribe(subscription -> req.end());
//    }
//
//    private static Function<Buffer, Long> extractingWoeid() {
//        return cityBuffer -> cityBuffer
//                .toJsonArray()
//                .getJsonObject(0)
//                .getLong("woeid");
//    }
//
//    private static Flowable<HttpClientResponse> getDataByPlaceId(HttpClient httpClient, long placeId) {
//        return autoPerformingReq(httpClient, String.format("/api/location/%s/", placeId));
//    }
//
//    private static Flowable<HttpClientResponse> autoPerformingReq(HttpClient httpClient, String uri) {
//        HttpClientRequest req = httpClient.get(new RequestOptions(metawether).setURI(uri));
//        return req.toFlowable().doOnSubscribe(subscription -> req.end());
//    }
//
//    private static Function<HttpClientResponse, Publisher<? extends Buffer>> toBufferFlowable() {
//        return response -> response
//                .toObservable()
//                .reduce(Buffer.buffer(), Buffer::appendBuffer).toFlowable();
//    }
//
//    private static Function<JsonObject, CityAndDayLength> toCityAndDayLength() {
//        return json -> {
//            ZonedDateTime sunRise = ZonedDateTime.parse(json.getString("sun_rise"));
//            ZonedDateTime sunSet = ZonedDateTime.parse(json.getString("sun_set"));
//            String cityName = json.getString("title");
//            return new CityAndDayLength(cityName, sunSet.toEpochSecond() - sunRise.toEpochSecond());
//        };
//    }
//}
