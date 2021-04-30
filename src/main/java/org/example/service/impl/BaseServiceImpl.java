package org.example.service.impl;

import io.vertx.core.Future;
import org.example.repository.Repository;
import org.example.repository.impl.AbstractRepository;
import org.example.service.BaseService;

public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final Repository<T, ID> repository = new AbstractRepository<>();

    @Override
    public Future<T> create(T entity) {
        return repository.save(entity);
    }

    @Override
    public Future<T> edit(T entity) {
        return repository.update(entity);
    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public T findById(ID id) {
        return null;
    }
}
