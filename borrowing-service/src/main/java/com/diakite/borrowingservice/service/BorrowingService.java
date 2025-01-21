package com.diakite.borrowingservice.service;

import com.diakite.borrowingservice.entity.Borrowing;
import com.diakite.borrowingservice.repository.BorrowingRepository;
import com.diakite.borrowingservice.dto.BorrowingResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public List<BorrowingResponseDTO> getAllBorrowings() {
        return borrowingRepository.findAll().stream()
                .map(this::enrichBorrowingWithUserAndBookDetails)
                .collect(Collectors.toList());
    }

    public BorrowingResponseDTO getBorrowingById(Long id) {
        return borrowingRepository.findById(id)
                .map(this::enrichBorrowingWithUserAndBookDetails)
                .orElse(null);
    }

    public BorrowingResponseDTO createBorrowing(Borrowing borrowing) {
        // Vérifier la disponibilité du livre via book-service
        Boolean isAvailable = restTemplate.getForObject(
                "http://book-service/api/v1/book/" + borrowing.getBookId() + "/available",
                Boolean.class
        );

        if (Boolean.TRUE.equals(isAvailable)) {
            // Vérifier si l'utilisateur peut emprunter via user-service
            Boolean canBorrow = restTemplate.getForObject(
                    "http://user-service/api/v1/user/" + borrowing.getUserId() + "/can-borrow",
                    Boolean.class
            );

            if (Boolean.TRUE.equals(canBorrow)) {
                Borrowing newBorrowing = new Borrowing.BorrowingBuilder()
                        .userId(borrowing.getUserId())
                        .bookId(borrowing.getBookId())
                        .build();
                
                Borrowing savedBorrowing = borrowingRepository.save(newBorrowing);
                
                // Notifier les autres services via Kafka
                kafkaTemplate.send("borrowing-events", "CREATE", savedBorrowing);
                
                return enrichBorrowingWithUserAndBookDetails(savedBorrowing);
            }
        }
        return null;
    }

    public BorrowingResponseDTO returnBook(Long id) {
        return borrowingRepository.findById(id)
                .map(borrowing -> {
                    borrowing.setReturned(true);
                    Borrowing updatedBorrowing = borrowingRepository.save(borrowing);
                    
                    // Notifier les autres services via Kafka
                    kafkaTemplate.send("borrowing-events", "RETURN", updatedBorrowing);
                    
                    return enrichBorrowingWithUserAndBookDetails(updatedBorrowing);
                })
                .orElse(null);
    }

    private BorrowingResponseDTO enrichBorrowingWithUserAndBookDetails(Borrowing borrowing) {
        // Récupérer les détails de l'utilisateur
        var user = restTemplate.getForObject(
                "http://user-service/api/v1/user/" + borrowing.getUserId(),
                Object.class
        );

        // Récupérer les détails du livre
        var book = restTemplate.getForObject(
                "http://book-service/api/v1/book/" + borrowing.getBookId(),
                Object.class
        );

        BorrowingResponseDTO dto = new BorrowingResponseDTO();
        dto.setId(borrowing.getId());
        dto.setUserId(borrowing.getUserId());
        dto.setBookId(borrowing.getBookId());
        dto.setBorrowingDate(borrowing.getBorrowingDate());
        dto.setReturnDate(borrowing.getReturnDate());
        dto.setReturned(borrowing.isReturned());
        
        if (user != null) {
            dto.setUserName(((java.util.Map)user).get("name").toString());
        }
        
        if (book != null) {
            dto.setBookTitle(((java.util.Map)book).get("title").toString());
        }

        return dto;
    }
} 