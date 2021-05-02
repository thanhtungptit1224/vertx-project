package org.example.service.impl;

import io.vertx.core.Future;
import org.example.repository.BaseRepository;
import org.example.repository.impl.BaseRepositoryImpl;
import org.example.service.BaseService;
import org.example.specification.Specification;

import java.util.List;

public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    protected final BaseRepository<T, ID> baseRepository;

    protected BaseServiceImpl(Class<T> clazz) {
        this.baseRepository = new BaseRepositoryImpl<>(clazz);
    }

    @Override
    public Future<T> create(T entity) {
        return baseRepository.save(entity);
    }

    @Override
    public Future<T> edit(T entity) {
        return baseRepository.update(entity);
    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public T findById(ID id) {
        return null;
    }

    @Override
    public Future<List<T>> search(Specification specification) {
        return baseRepository.find(specification);
    }
}
