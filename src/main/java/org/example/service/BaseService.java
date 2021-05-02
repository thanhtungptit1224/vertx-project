package org.example.service;

import io.vertx.core.Future;
import org.example.specification.Specification;

import java.util.List;

public interface BaseService<T, ID> {
    Future<T> create(T entity);

    Future<T> edit(T entity);

    void delete(ID id);

    Future<T> findById(ID id);

    Future<List<T>> search(Specification specification);
}
