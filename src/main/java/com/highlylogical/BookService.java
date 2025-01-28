package com.highlylogical;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final CassandraConnector connector;

    public BookService(CassandraConnector connector) {
        this.connector = connector;
    }

    public void addBook(Book book) {
        connector.connect().execute("INSERT INTO books (id, title, author) VALUES (?, ?, ?)",
                book.id(), book.title(), book.author());
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = connector.connect().execute("SELECT id, title, author FROM books");
        for (Row row : resultSet) {
            books.add(new Book(row.getString("id"), row.getString("title"), row.getString("author")));
        }
        return books;
    }

    public Book getBook(String id) {
        Row row = connector.connect().execute("SELECT id, title, author FROM books WHERE id = ?", id).one();
        if (row == null) return null;
        return new Book(row.getString("id"), row.getString("title"), row.getString("author"));
    }

    public void deleteBook(String id) {
        connector.connect().execute("DELETE FROM books WHERE id = ?", id);
    }
}