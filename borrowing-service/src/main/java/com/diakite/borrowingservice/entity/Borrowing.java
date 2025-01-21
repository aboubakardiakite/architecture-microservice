package com.diakite.borrowingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private boolean returned;

    private Borrowing(BorrowingBuilder builder) {
        this.userId = builder.userId;
        this.bookId = builder.bookId;
        this.borrowingDate = builder.borrowingDate;
        this.returnDate = builder.returnDate;
        this.returned = builder.returned;
    }

    public Borrowing() {
    }

    public static class BorrowingBuilder {
        private Long userId;
        private Long bookId;
        private LocalDate borrowingDate;
        private LocalDate returnDate;
        private boolean returned;

        public BorrowingBuilder() {
            this.borrowingDate = LocalDate.now();
            this.returnDate = LocalDate.now().plusDays(14); // Prêt par défaut de 14 jours
            this.returned = false;
        }

        public BorrowingBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public BorrowingBuilder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }

        public BorrowingBuilder borrowingDate(LocalDate borrowingDate) {
            this.borrowingDate = borrowingDate;
            return this;
        }

        public BorrowingBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowingBuilder returned(boolean returned) {
            this.returned = returned;
            return this;
        }

        public Borrowing build() {
            return new Borrowing(this);
        }
    }
} 