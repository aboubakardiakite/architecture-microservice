package com.diakite.borrowingservice.kafka;

import com.diakite.borrowingservice.service.BorrowingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BorrowingKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingKafkaConsumer.class);

    @Autowired
    private BorrowingService borrowingService;

    @KafkaListener(topics = "${spring.kafka.topic.user:user-events}", groupId = "borrowing-service")
    public void handleUserEvent(String event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(event);
            if ("USER_DELETED".equals(jsonNode.get("eventType").asText())) {
                Long userId = jsonNode.get("userId").asLong();
                borrowingService.deleteUserBorrowings(userId);
                logger.info("User delete event processed for userId: {}", userId);
            }
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.book:book-events}", groupId = "borrowing-service")
    public void handleBookEvent(String event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(event);
            if ("BOOK_DELETED".equals(jsonNode.get("eventType").asText())) {
                Long bookId = jsonNode.get("bookId").asLong();
                borrowingService.deleteBorrowingsByBookId(bookId);
                logger.info("Book delete event processed for bookId: {}", bookId);
            }
        } catch (Exception e) {
            logger.error("Error processing book event: {}", e.getMessage());
        }
    }
} 