package com.diakite.userservice.service;

import com.diakite.userservice.entity.User;
import com.diakite.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        User newUser = new User.UserBuilder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setBlocked(user.isBlocked());
                    existingUser.setMaxBooksAllowed(user.getMaxBooksAllowed());
                    existingUser.setCurrentBorrowedBooks(user.getCurrentBorrowedBooks());
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            // Envoyer un message Kafka pour supprimer les emprunts associés
            kafkaTemplate.send("user-events", "DELETE", id);
        });
    }

    public void updateBorrowingStatus(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            int currentBooks = user.getCurrentBorrowedBooks() + 1;
            user.setCurrentBorrowedBooks(currentBooks);
            if (currentBooks >= user.getMaxBooksAllowed()) {
                user.setBlocked(true);
            }
            userRepository.save(user);
        });
    }
} 