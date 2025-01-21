package com.diakite.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private boolean isBlocked;
    private int maxBooksAllowed;
    private int currentBorrowedBooks;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.isBlocked = builder.isBlocked;
        this.maxBooksAllowed = builder.maxBooksAllowed;
        this.currentBorrowedBooks = builder.currentBorrowedBooks;
    }

    public User() {
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private boolean isBlocked;
        private int maxBooksAllowed;
        private int currentBorrowedBooks;

        public UserBuilder() {
            this.maxBooksAllowed = 5; // Valeur par défaut
            this.isBlocked = false;
            this.currentBorrowedBooks = 0;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder blocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public UserBuilder maxBooksAllowed(int maxBooksAllowed) {
            this.maxBooksAllowed = maxBooksAllowed;
            return this;
        }

        public UserBuilder currentBorrowedBooks(int currentBorrowedBooks) {
            this.currentBorrowedBooks = currentBorrowedBooks;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
} 