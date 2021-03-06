package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.example.supperinterface.Service;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class ServiceFactory extends AbstractVerticle {

    private static final Map<String, Object> SERVICE_FACTORY = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.example.service.impl"))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        );

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Service.class)) {
            Class<?> entityClass = (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
            Object o = clazz.getDeclaredConstructor(Class.class).newInstance(entityClass);
            SERVICE_FACTORY.put(clazz.getInterfaces()[0].getSimpleName(), o);
        }

        startPromise.complete();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        return (T) SERVICE_FACTORY.get(clazz.getSimpleName());
    }
}
