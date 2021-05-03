package org.example.repository.impl;

import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.example.config.PostgreSqlConfig;
import org.example.repository.BaseRepository;
import org.example.specification.Specification;
import org.example.supperinterface.Column;
import org.example.supperinterface.Table;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class BaseRepositoryImpl<T, ID> implements BaseRepository<T, ID> {

    private final Class<T> clazz;
    private final PgPool pgPool;
    private final String table;
    private final Map<String, String> fieldColumn; // not contain id

    public BaseRepositoryImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.pgPool = PostgreSqlConfig.getPgPool();
        this.table = clazz.getDeclaredAnnotation(Table.class).value();
        this.fieldColumn = new HashMap<>();

        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.getName().equals("id"))
                continue;
            Column column = field.getDeclaredAnnotation(Column.class);
            this.fieldColumn.put(field.getName(), column.value());
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

    private void updateId(T entity, Object id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getId(T entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);

            return idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private T rowToEntity(Row row) {
        try {
            T entity = this.clazz.newInstance();

            for (Map.Entry<String, String> entry : fieldColumn.entrySet()) {
                Field field = this.clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(entity, row.getValue(entry.getValue()));
            }

            Object id = row.getValue("id");
            updateId(entity, id);

            return entity;
        } catch (Exception e) {
            log.info("Error When Getting Entity From Row: {}", row, e);
        }
        return null;
    }

    private String columnsClause(Collection<String> columns) {
        return "(" + String.join(",", columns) + ")";
    }

    private String valuesClause(int size) {
        return "(" + IntStream.range(1, size).mapToObj(value -> "$" + value).collect(Collectors.joining(",")) + ")";
    }

    private String insertQuery() {
        return "INSERT INTO " + table + columnsClause(fieldColumn.values()) + " VALUES" + valuesClause(fieldColumn.size() + 1) + " RETURNING id";
    }

    private String updateQuery() {
        List<String> condition = new ArrayList<>();
        int index = 1;
        for (String column : fieldColumn.values())
            condition.add(column + " = " + "$" + index++);

        return "UPDATE " + table + " SET " + String.join(", ", condition) + " WHERE id=$" + index;
    }

    @Override
    public Future<T> save(T entity) {
        return pgPool
                .preparedQuery(insertQuery())
                .execute(entityToTuple(entity))
                .map(rows -> {
                    if (rows.rowCount() != 1)
                        return null;
                    updateId(entity, rows.iterator().next().getLong("id"));
                    return entity;
                });
    }

    @Override
    public Future<T> update(T entity) {
        Tuple tuple = entityToTuple(entity);
        tuple.addValue(getId(entity));

        return pgPool
                .preparedQuery(updateQuery())
                .execute(tuple)
                .map(rows -> {
                    if (rows.rowCount() != 1)
                        return null;
                    return entity;
                });
    }

    @Override
    public Future<ID> delete(ID id) {
        return pgPool
                .query("DELETE FROM " + table + " WHERE id = " + id)
                .execute()
                .map(rows -> {
                    if (rows.rowCount() != 1)
                        return null;
                    return id;
                });
    }

    @Override
    public Future<T> findById(ID id) {
        return pgPool
                .query("SELECT * FROM " + table + " WHERE id = " + id)
                .execute()
                .map(rows -> {
                    if (rows.rowCount() != 1)
                        return null;
                    return rowToEntity(rows.iterator().next());
                });
    }

    @Override
    public Future<List<T>> find(Specification specification) {
        String selectClause = "SELECT * FROM " + table;
        if (!specification.getCondition().isEmpty())
            selectClause += " WHERE " + String.join(" AND ", specification.getCondition());
        return pgPool
                .query(selectClause)
                .execute()
                .map(rows -> {
                    List<T> data = new ArrayList<>();
                    for (Row row : rows) {
                        T entity = rowToEntity(row);
                        if (entity != null)
                            data.add(entity);
                    }
                    return data;
                });
    }
}
