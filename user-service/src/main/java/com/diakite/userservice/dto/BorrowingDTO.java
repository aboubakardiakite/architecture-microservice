package com.diakite.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private boolean returned;
} 