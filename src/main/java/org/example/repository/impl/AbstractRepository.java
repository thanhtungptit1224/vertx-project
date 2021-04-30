package org.example.repository.impl;

import io.vertx.core.Future;
import io.vertx.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.example.config.PostgreSqlConfig;
import org.example.repository.Repository;
import org.example.supperinterface.Column;
import org.example.supperinterface.Table;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {

    private final PostgreSqlConfig db;
    private final String table;
    private final List<String> columns;

    {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<?> clazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];

        System.out.println("Start initial abstract repository: " + clazz.getName());

        db = PostgreSqlConfig.getInstance();
        table = clazz.getAnnotation(Table.class).value();
        columns = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("id"))
                continue;
            Column column = field.getDeclaredAnnotation(Column.class);
            columns.add(column.value());
        }

        System.out.println("Initial abstract repository success: " + clazz.getName());
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

    private String insert() {
        return "INSERT INTO " + table + columnsClause(columns) + " VALUES" + valuesClause(columns.size() + 1) + " RETURNING id";
    }

    @Override
    public Future<T> save(T entity) {
        return db.getClient()
                .preparedQuery(insert())
                .execute(entityToTuple(entity))
                .map(rows -> {
                    updateId(entity, rows.iterator().next().getLong("id"));
                    return entity;
                });
    }

    @Override
    public T update(T entity) {
        return null;
    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public T findById(ID id) {
        return null;
    }
}
