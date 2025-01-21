package com.diakite.borrowingservice.client;

import com.diakite.borrowingservice.dto.BookDTO;
import com.diakite.borrowingservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BOOK_SERVICE_URL = "http://book-service/api/v1/book";
    private static final String USER_SERVICE_URL = "http://user-service/api/v1/user";

    public BookDTO getBook(Long bookId) {
        return restTemplate.getForObject(BOOK_SERVICE_URL + "/{id}", BookDTO.class, bookId);
    }

    public UserDTO getUser(Long userId) {
        return restTemplate.getForObject(USER_SERVICE_URL + "/{id}", UserDTO.class, userId);
    }

    public Boolean checkBookAvailability(Long bookId) {
        try {
            System.out.println("Calling book service: " + BOOK_SERVICE_URL + "/{id}/available");
            Boolean result = restTemplate.getForObject(BOOK_SERVICE_URL + "/{id}/available", Boolean.class, bookId);
            System.out.println("Result from book service: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error calling book service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean checkUserCanBorrow(Long userId) {
        return restTemplate.getForObject(USER_SERVICE_URL + "/{id}/can-borrow", Boolean.class, userId);
    }
} 