package com.diakite.bookservice.service;

import com.diakite.bookservice.dto.BookDTO;
import com.diakite.bookservice.entity.Book;
import com.diakite.bookservice.exception.BookNotFoundException;
import com.diakite.bookservice.kafka.BookKafkaProducer;
import com.diakite.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
    }

    public Book createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(bookDTO.getCategory());
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
            
        bookRepository.delete(book);
        kafkaProducer.sendBookDeleteEvent(id);
    }

    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id)
            .map(book -> {
                book.setTitle(bookDetails.getTitle());
                book.setAuthor(bookDetails.getAuthor());
                book.setCategory(bookDetails.getCategory());
                book.setAvailable(bookDetails.isAvailable());
                return bookRepository.save(book);
            })
            .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
    }

    public void updateBookAvailability(Long id, boolean isAvailable) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
            
        book.setAvailable(isAvailable);
        bookRepository.save(book);
    }
} 