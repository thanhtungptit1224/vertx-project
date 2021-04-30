package org.example.entity;

import lombok.Data;
import org.example.supperinterface.Column;
import org.example.supperinterface.Table;

@Data
@Table("book")
public class Book {
    @Column("id")
    private Long   id;

    @Column("name")
    private String name;

    @Column("author")
    private String author;

    @Column("category")
    private String category;
}
