package com.diakite.borrowingservice.repository;

import com.diakite.borrowingservice.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUserId(Long userId);
    List<Borrowing> findByBookId(Long bookId);
    List<Borrowing> findByUserIdAndReturned(Long userId, boolean returned);
} 