package org.example.repository;

import io.vertx.core.Future;

public interface Repository<T, ID> {
    Future<T> save(T entity);

    Future<T> update(T entity);

    void delete(ID id);

    T findById(ID id);

//    Future<T> find(Query<T> query);
//
//    Future<List<T>> findAll(Query<T> query);
//
//    Future<Page<T>> findAll(Query<T> query, PageAble pageAble);
//
//    Future<Long> count(Query<T> query);
}
