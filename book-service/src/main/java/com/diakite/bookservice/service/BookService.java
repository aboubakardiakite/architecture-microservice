package com.diakite.bookservice.service;

import com.diakite.bookservice.dto.BookDTO;
import com.diakite.bookservice.entity.Book;
import com.diakite.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(BookDTO book) {
        Book book1 = new Book();
        book1.setAuthor(book.getAuthor());
        book1.setAvailable(book.isAvailable());
        book1.setCategory(book.getCategory());
        book1.setTitle(book.getTitle());

        return bookRepository.save(book1);
    }

    public Book updateBook(Long id, Book book) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            book.setId(id);
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            bookRepository.delete(book);
            // Envoyer un message Kafka pour supprimer les emprunts associés
            kafkaTemplate.send("book-events", "DELETE", id);
        });
    }

    public void updateBookAvailability(Long id, boolean isAvailable) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setAvailable(isAvailable);
            bookRepository.save(book);
        });
    }
} 