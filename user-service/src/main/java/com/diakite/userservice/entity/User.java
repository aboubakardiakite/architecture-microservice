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
    
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;
    
    private boolean isLocked;
    private Integer nombreMaxEmprunt;
    private Integer currentBorrowedBooks;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.membershipType = builder.membershipType;
        this.isLocked = builder.isLocked;
        this.nombreMaxEmprunt = builder.nombreMaxEmprunt;
        this.currentBorrowedBooks = builder.currentBorrowedBooks;
    }

    public User() {
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private MembershipType membershipType;
        private boolean isLocked;
        private Integer nombreMaxEmprunt;
        private Integer currentBorrowedBooks;

        public UserBuilder() {
            this.membershipType = MembershipType.REGULAR;
            this.isLocked = false;
            this.nombreMaxEmprunt = 5; // Par défaut pour REGULAR
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

        public UserBuilder membershipType(MembershipType membershipType) {
            this.membershipType = membershipType;
            this.nombreMaxEmprunt = (membershipType == MembershipType.PREMIUM) ? 7 : 5;
            return this;
        }

        public UserBuilder locked(boolean isLocked) {
            this.isLocked = isLocked;
            return this;
        }

        public UserBuilder currentBorrowedBooks(Integer currentBorrowedBooks) {
            this.currentBorrowedBooks = currentBorrowedBooks;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
} 