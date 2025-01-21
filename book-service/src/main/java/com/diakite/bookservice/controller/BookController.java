package com.diakite.bookservice.controller;

import com.diakite.bookservice.dto.BookDTO;
import com.diakite.bookservice.entity.Book;
import com.diakite.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@Tag(name = "Book Controller", description = "API pour la gestion des livres")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Récupérer tous les livres")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Récupérer un livre par son ID")
    @ApiResponse(responseCode = "200", description = "Livre trouvé")
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Créer un nouveau livre")
    @PostMapping
    public Book createBook(@RequestBody BookDTO book) {
        return bookService.createBook(book);
    }

    @Operation(summary = "Mettre à jour un livre")
    @ApiResponse(responseCode = "200", description = "Livre mis à jour")
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Supprimer un livre")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Vérifier la disponibilité d'un livre")
    @ApiResponse(responseCode = "200", description = "Disponibilité vérifiée")
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isBookAvailable(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book.isAvailable());
        }
        return ResponseEntity.notFound().build();
    }
} 