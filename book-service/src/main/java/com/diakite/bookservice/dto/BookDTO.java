package com.diakite.bookservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
} 