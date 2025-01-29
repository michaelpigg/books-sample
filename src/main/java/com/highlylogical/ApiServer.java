package com.highlylogical;

import io.javalin.Javalin;
import io.javalin.util.ConcurrencyUtil;
import org.crac.Context;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ApiServer implements Resource {
    public static final Logger log = LoggerFactory.getLogger(ApiServer.class);
    private final BookService bookService;
    private Javalin app;
    public ApiServer(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
	log.info("Stopping Javalin server before checkpoint");
        this.stop();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
	log.info("Starting Javalin server after checkpoint");
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
