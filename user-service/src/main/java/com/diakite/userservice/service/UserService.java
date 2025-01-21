package com.diakite.userservice.service;

import com.diakite.userservice.entity.User;
import com.diakite.userservice.entity.MembershipType;
import com.diakite.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        int maxBooks = (user.getMembershipType() == MembershipType.PREMIUM) ? 7 : 5;
        
        User newUser = new User.UserBuilder()
                .name(user.getName())
                .email(user.getEmail())
                .membershipType(user.getMembershipType())
                .build();
                
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setMembershipType(user.getMembershipType());
                    existingUser.setLocked(user.isLocked());
                    existingUser.setCurrentBorrowedBooks(user.getCurrentBorrowedBooks());
                    
                    // Mise à jour du nombre max d'emprunts selon le type d'adhésion
                    int maxBooks = (user.getMembershipType() == MembershipType.PREMIUM) ? 7 : 5;
                    existingUser.setNombreMaxEmprunt(maxBooks);
                    
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean canBorrow(Long userId) {
        return userRepository.findById(userId)
                .map(user -> !user.isLocked() && 
                     user.getCurrentBorrowedBooks() < user.getNombreMaxEmprunt())
                .orElse(false);
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
} 