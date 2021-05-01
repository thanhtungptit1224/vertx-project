package org.example.repository.impl;

import org.example.entity.Book;
import org.example.repository.BookRepository;

public class BookRepositoryImpl extends BaseRepositoryImpl<Book, Long> implements BookRepository {
    public BookRepositoryImpl(Class<Book> clazz) {
        super(clazz);
    }
}
