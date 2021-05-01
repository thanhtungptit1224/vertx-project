package org.example.service.impl;

import org.example.entity.Book;
import org.example.repository.BookRepository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.service.BookService;
import org.example.supperinterface.Service;

@Service
public final class BookServiceImpl extends BaseServiceImpl<Book, Long> implements BookService {
    private final BookRepository bookRepository;
    public BookServiceImpl(Class<Book> clazz) {
        super(clazz);
        bookRepository = new BookRepositoryImpl(clazz);
    }
}
