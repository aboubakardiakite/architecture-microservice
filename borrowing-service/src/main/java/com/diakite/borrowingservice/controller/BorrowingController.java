package com.diakite.borrowingservice.controller;

import com.diakite.borrowingservice.entity.Borrowing;
import com.diakite.borrowingservice.service.BorrowingService;
import com.diakite.borrowingservice.dto.BorrowingResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @GetMapping
    public List<BorrowingResponseDTO> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponseDTO> getBorrowingById(@PathVariable Long id) {
        BorrowingResponseDTO borrowing = borrowingService.getBorrowingById(id);
        return borrowing != null ? ResponseEntity.ok(borrowing) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BorrowingResponseDTO> createBorrowing(@RequestBody Borrowing borrowing) {
        BorrowingResponseDTO newBorrowing = borrowingService.createBorrowing(borrowing);
        return newBorrowing != null ? ResponseEntity.ok(newBorrowing) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowingResponseDTO> returnBook(@PathVariable Long id) {
        BorrowingResponseDTO returnedBorrowing = borrowingService.returnBook(id);
        return returnedBorrowing != null ? ResponseEntity.ok(returnedBorrowing) : ResponseEntity.notFound().build();
    }
} 