package com.diakite.borrowingservice.dto;

import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private MembershipType membershipType;
    private boolean isLocked;
    private Integer nombreMaxEmprunt;
} 