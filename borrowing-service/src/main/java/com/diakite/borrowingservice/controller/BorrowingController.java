package com.diakite.borrowingservice.controller;

import com.diakite.borrowingservice.entity.Borrowing;
import com.diakite.borrowingservice.service.BorrowingService;
import com.diakite.borrowingservice.dto.BorrowingResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowing")
@Tag(name = "Borrowing Controller", description = "API pour la gestion des emprunts")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @Operation(summary = "Récupérer tous les emprunts")
    @GetMapping
    public List<BorrowingResponseDTO> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @Operation(summary = "Récupérer un emprunt par son ID")
    @ApiResponse(responseCode = "200", description = "Emprunt trouvé")
    @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponseDTO> getBorrowingById(@PathVariable Long id) {
        BorrowingResponseDTO borrowing = borrowingService.getBorrowingById(id);
        return borrowing != null ? ResponseEntity.ok(borrowing) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Créer un nouvel emprunt")
    @PostMapping
    public ResponseEntity<BorrowingResponseDTO> createBorrowing(@RequestBody Borrowing borrowing) {
        BorrowingResponseDTO newBorrowing = borrowingService.createBorrowing(borrowing);
        return newBorrowing != null ? ResponseEntity.ok(newBorrowing) : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Retourner un livre emprunté")
    @ApiResponse(responseCode = "200", description = "Livre retourné avec succès")
    @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowingResponseDTO> returnBook(@PathVariable Long id) {
        BorrowingResponseDTO returnedBorrowing = borrowingService.returnBook(id);
        return returnedBorrowing != null ? ResponseEntity.ok(returnedBorrowing) : ResponseEntity.notFound().build();
    }
} 