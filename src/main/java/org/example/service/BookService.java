package org.example.service;

import io.vertx.core.Future;
import org.example.entity.Book;

public interface BookService {
     Future<Book> create(Book book);
}
