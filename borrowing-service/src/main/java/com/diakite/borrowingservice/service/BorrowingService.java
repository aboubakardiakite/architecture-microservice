package com.diakite.borrowingservice.service;

import com.diakite.borrowingservice.client.RestClient;
import com.diakite.borrowingservice.dto.BookDTO;
import com.diakite.borrowingservice.dto.UserDTO;
import com.diakite.borrowingservice.entity.Borrowing;
import com.diakite.borrowingservice.kafka.BorrowingKafkaProducer;
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
    BorrowingKafkaProducer borrowingKafkaProducer;


    @Autowired
    RestClient restClient;

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
        Boolean isAvailable = restClient.checkBookAvailability(borrowing.getBookId());

        System.out.println("BorrowingController.createBorrowing"+isAvailable);


        if (Boolean.TRUE.equals(isAvailable)) {
            // Vérifier si l'utilisateur peut emprunter via user-service
            Boolean canBorrow = restClient.checkUserCanBorrow(borrowing.getUserId());

            if (Boolean.TRUE.equals(canBorrow)) {
                Borrowing newBorrowing = new Borrowing.BorrowingBuilder()
                        .userId(borrowing.getUserId())
                        .bookId(borrowing.getBookId())
                        .build();
                
                Borrowing savedBorrowing = borrowingRepository.save(newBorrowing);
                

                borrowingKafkaProducer.sendBorrowingCreateEvent(savedBorrowing.getId(), savedBorrowing.getUserId(), savedBorrowing.getBookId());
                return enrichBorrowingWithUserAndBookDetails(savedBorrowing);
            }
        }
        return null;
    }

    public BorrowingResponseDTO returnBook(Long id) {

        return borrowingRepository.findById(id)
                .map(borrowing -> {

                    deleteUserBorrowings(borrowing.getUserId());

                    borrowingKafkaProducer.sendBorrowingReturnEvent( borrowing.getUserId(), borrowing.getBookId());
                    borrowingKafkaProducer.sendBorrowingDeleteEvent(borrowing.getBookId());

                    return enrichBorrowingWithUserAndBookDetails(borrowing);
                })
                .orElse(null);
    }




    private BorrowingResponseDTO enrichBorrowingWithUserAndBookDetails(Borrowing borrowing) {
        // Récupérer les détails de l'utilisateur
        UserDTO user = restClient.getUser(borrowing.getUserId());

        // Récupérer les détails du livre
        BookDTO book = restClient.getBook(borrowing.getBookId());

        BorrowingResponseDTO dto = new BorrowingResponseDTO();
        dto.setId(borrowing.getId());
        dto.setUserId(borrowing.getUserId());
        dto.setBookId(borrowing.getBookId());
        dto.setBorrowingDate(borrowing.getBorrowingDate());
        dto.setReturnDate(borrowing.getReturnDate());
        dto.setReturned(borrowing.isReturned());
        
        if (user != null) {
            dto.setUserName(user.getName());
        }
        
        if (book != null) {
            dto.setBookTitle(book.getTitle());
        }

        return dto;
    }

    public void deleteBorrowingsByBookId(Long bookId) {

        borrowingRepository.findByBookId(bookId).forEach(borrowing -> {
            borrowingRepository.delete(borrowing);
            borrowingKafkaProducer.sendBorrowingReturnEvent(borrowing.getUserId(),borrowing.getBookId());
        });

    }

    public void deleteUserBorrowings(Long userId) {

        borrowingRepository.findByUserId(userId).forEach(borrowing -> {
            borrowingRepository.delete(borrowing);
            borrowingKafkaProducer.sendBorrowingDeleteEvent(borrowing.getBookId());
        });

    }
}