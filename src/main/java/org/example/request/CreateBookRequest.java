package org.example.request;

import lombok.Data;

@Data
public class CreateBookRequest {
    private String name;
    private String author;
    private String category;
}
