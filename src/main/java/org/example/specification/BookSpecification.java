package org.example.specification;

import org.example.request.SearchBookRequest;
import org.example.util.StringUtil;

public class BookSpecification extends AbstractSpecification implements Specification {

    public BookSpecification(SearchBookRequest request) {
        if (request.getId() != null)
            equal("id", request.getId());

        if (StringUtil.isNotBlank(request.getName()))
            equal("name", request.getName());
    }

}
