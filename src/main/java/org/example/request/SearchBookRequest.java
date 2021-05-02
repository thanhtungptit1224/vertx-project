package org.example.request;

import lombok.Data;

@Data
public class SearchBookRequest extends BaseRequest {
    private Long    id;
    private String  name;
}
