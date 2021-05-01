package org.example.repository.impl;

import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.example.config.PostgreSqlConfig;
import org.example.repository.BaseRepository;
import org.example.supperinterface.Column;
import org.example.supperinterface.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class BaseRepositoryImpl<T, ID> implements BaseRepository<T, ID> {

    private final PgPool pgPool;
    private final String table;
    private final List<String> columns;

    public BaseRepositoryImpl(Class<T> clazz) {
        pgPool = PostgreSqlConfig.getPgPool();
        table = clazz.getDeclaredAnnotation(Table.class).value();
        columns = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("id"))
                continue;
            Column column = field.getDeclaredAnnotation(Column.class);
            columns.add(column.value());
        }
    }

    private Tuple entityToTuple(T entity) {
        Tuple tuple = Tuple.tuple();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getName().equals("id"))
                continue;
            field.setAccessible(true);
            Object o = null;

            try {
                o = field.get(entity);
            } catch (Exception e) {
                log.error("Get Field Error {}", field.getName(), e);
            } finally {
                tuple.addValue(o);
            }
        }

        return tuple;
    }

    private void updateId(T entity, Long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String columnsClause(List<String> columns) {
        return "(" + String.join(",", columns) + ")";
    }

    private String valuesClause(int size) {
        return "(" + IntStream.range(1, size).mapToObj(value -> "$" + value).collect(Collectors.joining(",")) + ")";
    }

    private String insertQuery() {
        return "INSERT INTO " + table + columnsClause(columns) + " VALUES" + valuesClause(columns.size() + 1) + " RETURNING id";
    }

    private String updateQuery() {
        List<String> condition = new ArrayList<>();
        int index = 1;
        for (String column : columns)
            condition.add(column + " = " + "$" + index++);

        return "UPDATE " + table + " SET " + String.join(", ", condition) + " WHERE id=$" + index;
    }

    @Override
    public Future<T> save(T entity) {
        return pgPool
                .preparedQuery(insertQuery())
                .execute(entityToTuple(entity))
                .map(rows -> {
                    updateId(entity, rows.iterator().next().getLong("id"));
                    return entity;
                });
    }

    @Override
    public Future<T> update(T entity) {
        Long id = null;
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            id = (Long) idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Tuple tuple = entityToTuple(entity);
        tuple.addValue(id);

        return pgPool
                .preparedQuery(updateQuery())
                .execute(tuple)
                .map(rows -> entity);
    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public T findById(ID id) {
        return null;
    }
}