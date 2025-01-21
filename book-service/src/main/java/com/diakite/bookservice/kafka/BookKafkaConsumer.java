package com.diakite.bookservice.kafka;

import com.diakite.bookservice.service.BookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BookKafkaConsumer.class);

    @Autowired
    private BookService bookService;

    @KafkaListener(topics = "${spring.kafka.topic.borrowing:borrowing-events}", groupId = "book-service")
    public void handleBorrowingEvent(String event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(event);
            String eventType = jsonNode.get("eventType").asText();
            Long bookId = jsonNode.get("bookId").asLong();

            switch (eventType) {
                case "BORROWING_CREATED":
                    bookService.updateBookAvailability(bookId, false);
                    logger.info("Book availability updated to false for bookId: {}", bookId);
                    break;
                case "BORROWING_DELETED":
                    bookService.updateBookAvailability(bookId, true);
                    logger.info("Book availability updated to true for bookId: {}", bookId);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing borrowing event: {}", e.getMessage());
        }
    }

} 