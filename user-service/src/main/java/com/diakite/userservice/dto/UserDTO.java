package com.diakite.userservice.dto;

import com.diakite.userservice.entity.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private MembershipType membershipType;
    private boolean isLocked;
    private Integer nombreMaxEmprunt;
    private Integer currentBorrowedBooks;
} 