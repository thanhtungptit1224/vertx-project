package org.example.handler;

import io.vertx.ext.web.RoutingContext;
import org.example.entity.Book;
import org.example.request.CreateBookRequest;
import org.example.request.EditBookRequest;
import org.example.request.SearchBookRequest;
import org.example.service.BookService;
import org.example.specification.BookSpecification;
import org.example.specification.Specification;
import org.example.verticle.ServiceFactory;

public class BookHandler {
    public static void create(RoutingContext ctx) {
        CreateBookRequest request = ctx.getBodyAsJson().mapTo(CreateBookRequest.class);
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());

        BookService bookService = ServiceFactory.getInstance(BookService.class);
        bookService.create(book).onSuccess(book1 -> {
            if (book1 == null)
                ctx.response().setStatusCode(500).end("Internal Error");
            else
                ctx.response().end(book1.toString());
        });
    }

    public static void edit(RoutingContext ctx) {
        EditBookRequest request = ctx.getBodyAsJson().mapTo(EditBookRequest.class);
        Book book = new Book();
        book.setId(request.getId());
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());

        BookService bookService = ServiceFactory.getInstance(BookService.class);
        bookService.edit(book).onSuccess(book1 -> {
            if (book1 == null)
                ctx.response().setStatusCode(500).end("Internal Error");
            else
                ctx.response().end(book1.toString());
        });
    }

    public static void get(RoutingContext ctx) {
        BookService bookService = ServiceFactory.getInstance(BookService.class);
        bookService.findById(Long.valueOf(ctx.pathParam("id"))).onSuccess(book1 -> {
            if (book1 == null)
                ctx.response().setStatusCode(404).end("NotFound");
            else
                ctx.response().end(book1.toString());
        });
    }

    public static void delete(RoutingContext ctx) {
        BookService bookService = ServiceFactory.getInstance(BookService.class);
        bookService.delete(Long.valueOf(ctx.pathParam("id"))).onSuccess(id -> {
            if (id == null)
                ctx.response().setStatusCode(500).end("Internal Error");
            else
                ctx.response().end(id.toString());
        });
    }

    public static void search(RoutingContext ctx) {
        SearchBookRequest request = ctx.getBodyAsJson().mapTo(SearchBookRequest.class);
        Specification specification = new BookSpecification(request);

        BookService bookService = ServiceFactory.getInstance(BookService.class);
        bookService.search(specification).onSuccess(book1 -> ctx.response().end(book1.toString()));
    }
}
