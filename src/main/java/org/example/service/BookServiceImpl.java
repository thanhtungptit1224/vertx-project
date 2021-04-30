package org.example.service;

import io.vertx.core.Future;
import org.example.entity.Book;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepository;

public final class BookServiceImpl implements BookService {

    private static final BookServiceImpl INSTANCE = new BookServiceImpl();
    private final Repository<Book, Long> repository = new BookRepository();

    private BookServiceImpl() {
    }

    public static BookServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Future<Book> create(Book book) {
        return repository.save(book);
    }

}
