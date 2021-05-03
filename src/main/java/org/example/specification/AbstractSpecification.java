package org.example.specification;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSpecification implements Specification {
    private final List<String> condition = new ArrayList<>();
    private final List<String> order = new ArrayList<>();

    @Override
    public List<String> getCondition() {
        return condition;
    }

    @Override
    public List<String> getOrder() {
        return order;
    }

    public void equal(String column, Object value) {
        if (value instanceof String)
            value = "'" + value + "'";
        condition.add(column + Expression.equal() + value);
    }
}
