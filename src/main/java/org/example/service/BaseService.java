package org.example.service;

import io.vertx.core.Future;

public interface BaseService<T, ID> {
    Future<T> create(T entity);

    Future<T> edit(T entity);

    void delete(ID id);

    T findById(ID id);
}
