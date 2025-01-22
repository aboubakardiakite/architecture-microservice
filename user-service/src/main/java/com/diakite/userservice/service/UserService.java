package com.diakite.userservice.service;

import com.diakite.userservice.client.RestClient;
import com.diakite.userservice.entity.User;
import com.diakite.userservice.entity.MembershipType;
import com.diakite.userservice.kafka.UserKafkaProducer;
import com.diakite.userservice.repository.UserRepository;
import com.diakite.userservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RestClient restClient;

    @Autowired
    private UserKafkaProducer kafkaProducer;
    @Autowired
    private UserKafkaProducer userKafkaProducer;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public User createUser(User user) {

        User newUser = new User.UserBuilder()
                .name(user.getName())
                .email(user.getEmail())
                .membershipType(user.getMembershipType())
                .build();

        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
            .map(user -> {
                user.setName(userDetails.getName());
                user.setEmail(userDetails.getEmail());
                user.setMembershipType(userDetails.getMembershipType());
                user.setLocked(userDetails.isLocked());
                user.setCurrentBorrowedBooks(userDetails.getCurrentBorrowedBooks());
                
                // Mise à jour du nombre max d'emprunts selon le type d'adhésion
                int maxBooks = (userDetails.getMembershipType() == MembershipType.PREMIUM) ? 7 : 5;
                user.setNombreMaxEmprunt(maxBooks);

                userKafkaProducer.sendUserUpdateEvent(id);

                return userRepository.save(user);
            })
            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
            
        userRepository.deleteUserById(user.getId());
        kafkaProducer.sendUserDeleteEvent(id);
    }

    public boolean canBorrow(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
            
        return !user.isLocked() && user.getCurrentBorrowedBooks() < user.getNombreMaxEmprunt();
    }

    public void updateBorrowingStatus(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            int currentBooks = user.getCurrentBorrowedBooks() + 1;
            user.setCurrentBorrowedBooks(currentBooks);
            
            // Vérifier si l'utilisateur a atteint sa limite
            if (currentBooks >= user.getNombreMaxEmprunt()) {
                user.setLocked(true);
            }
            
        
            userRepository.save(user);
        });
    }

    public void unBorrowBook(Long userId) {

        userRepository.findById(userId).ifPresent(user -> {
            int currentBooks = user.getCurrentBorrowedBooks() - 1;
            user.setCurrentBorrowedBooks(currentBooks);

            // Déverrouiller l'utilisateur s'il était bloqué
            if (user.isLocked() && currentBooks < user.getNombreMaxEmprunt()) {
                user.setLocked(false);
            }
            userRepository.save(user);
        });


    }
}