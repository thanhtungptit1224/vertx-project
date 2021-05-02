package org.example.specification;

import java.util.List;

public interface Specification {
    List<String> getCondition();
    List<String> getOrder();
}
