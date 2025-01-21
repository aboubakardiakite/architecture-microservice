package com.diakite.bookservice.service;

import com.diakite.bookservice.dto.BookDTO;
import com.diakite.bookservice.entity.Book;
import com.diakite.bookservice.kafka.BookKafkaProducer;
import com.diakite.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookKafkaProducer kafkaProducer;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(bookDTO.getCategory());
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book book) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setAuthor(book.getAuthor());
                    existingBook.setCategory(book.getCategory());
                    existingBook.setAvailable(book.isAvailable());
                    return bookRepository.save(existingBook);
                })
                .orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            bookRepository.delete(book);
            kafkaProducer.sendBookDeleteEvent(id);
        });
    }

    public void updateBookAvailability(Long id, boolean isAvailable) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setAvailable(isAvailable);
            bookRepository.save(book);
        });
    }
} 