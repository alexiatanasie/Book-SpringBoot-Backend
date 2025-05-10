package com.example.bookappbackend.controller;

import com.example.bookappbackend.model.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class BookController {
    private Map<Long, Book> bookStorage;

    @PostConstruct
    public void init() {
        bookStorage = new ConcurrentHashMap<>();
    }

    // create
    //post book
    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        if (book.getBookId() == null ||
                book.getTitle() == null || book.getAuthor() == null || book.getCover() == null ||
                book.getDescription() == null || book.getGenre() == null || book.getTotalPages() == null ||
                book.getPublicationDate() == null || book.getAddedBy() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (bookStorage.containsKey(book.getBookId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        bookStorage.put(book.getBookId(), book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    //read
    //get all books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(new ArrayList<>(bookStorage.values()));
    }

    //get one book by id
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Book book = bookStorage.get(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(book);
    }

    //update
    //put (update by id)
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book updatedBook) {
        if (!bookStorage.containsKey(id)) {
            return ResponseEntity.badRequest().build();
        }

        if (updatedBook.getTitle() == null || updatedBook.getAuthor() == null || updatedBook.getCover() == null ||
                updatedBook.getDescription() == null || updatedBook.getGenre() == null || updatedBook.getTotalPages() == null ||
                updatedBook.getPublicationDate() == null || updatedBook.getAddedBy() == null) {
            return ResponseEntity.badRequest().build();
        }

        updatedBook.setBookId(id);
        bookStorage.put(id, updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    //delete book by id
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        if (!bookStorage.containsKey(id)) {
            return ResponseEntity.badRequest().build();
        }
        bookStorage.remove(id);
        return ResponseEntity.noContent().build();
    }
}
