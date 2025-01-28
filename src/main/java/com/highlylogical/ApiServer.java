package com.highlylogical;

import io.javalin.Javalin;
import org.crac.Context;
import org.crac.Resource;

import java.util.List;

public class ApiServer implements Resource {
    private final BookService bookService;
    private Javalin app;
    public ApiServer(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        this.stop();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        this.start();
    }

    public void stop() {
        app.stop();
    }

    public void start() {
        app = Javalin.create().start(8080);

        app.get("/books", ctx -> {
            List<Book> books = bookService.getAllBooks();
            ctx.json(books);
        });

        app.get("/books/{id}", ctx -> {
            Book book = bookService.getBook(ctx.pathParam("id"));
            if (book == null) ctx.status(404).result("Book not found");
            else ctx.json(book);
        });

        app.post("/books", ctx -> {
            Book book = ctx.bodyAsClass(Book.class);
            bookService.addBook(book);
            ctx.status(201);
        });

        app.delete("/books/{id}", ctx -> {
            bookService.deleteBook(ctx.pathParam("id"));
            ctx.status(204);
        });
    }
}