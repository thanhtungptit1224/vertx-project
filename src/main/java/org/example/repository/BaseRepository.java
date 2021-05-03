package org.example.repository;

import io.vertx.core.Future;
import org.example.specification.Specification;

import java.util.List;

public interface BaseRepository<T, ID> {
    Future<T> save(T entity);

    Future<T> update(T entity);

    Future<ID> delete(ID id);

    Future<T> findById(ID id);

    Future<List<T>> find(Specification specification);

//    Future<List<T>> findAll(Query<T> query);
//
//    Future<Page<T>> findAll(Query<T> query, PageAble pageAble);
//
//    Future<Long> count(Query<T> query);
}
