package com.diakite.userservice.client;

import com.diakite.userservice.dto.BorrowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String borrowingServiceUrl = "http://borrowing-service/api/v1/borrowing";

    public List<BorrowingDTO> getUserBorrowings(Long userId) {
        try {
            String url = borrowingServiceUrl + "/user/" + userId;
            BorrowingDTO[] borrowings = restTemplate.getForObject(url, BorrowingDTO[].class);
            return borrowings != null ? Arrays.asList(borrowings) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
} 