package com.diakite.bookservice.client;

import com.diakite.bookservice.dto.BorrowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String borrowingServiceUrl = "http://borrowing-service/api/v1/borrowing/";

    public List<BorrowingDTO> getBorrowingsByBookId(Long bookId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(borrowingServiceUrl)
                    .path("book/")
                    .path(String.valueOf(bookId))
                    .toUriString();

            BorrowingDTO[] borrowings = restTemplate.getForObject(url, BorrowingDTO[].class);
            return borrowings != null ? Arrays.asList(borrowings) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error while fetching borrowings: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
