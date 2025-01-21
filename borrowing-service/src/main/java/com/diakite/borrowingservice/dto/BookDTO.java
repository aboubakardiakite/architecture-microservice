package com.diakite.borrowingservice.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
} 