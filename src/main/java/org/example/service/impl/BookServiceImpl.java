package org.example.service.impl;

import io.vertx.core.Future;
import org.example.entity.Book;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepository;
import org.example.service.BookService;
import org.example.supperinterface.Service;

@Service
public final class BookServiceImpl extends BaseServiceImpl implements BookService {

    private final Repository<Book, Long> repository = new BookRepository();

    @Override
    public Future<Book> create(Book book) {
        return repository.save(book);
    }

    @Override
    public Future<Book> edit(Book book) {
        return repository.update(book);
    }
}
