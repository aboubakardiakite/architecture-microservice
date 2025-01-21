package com.diakite.userservice.kafka;

import com.diakite.userservice.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserKafkaConsumer.class);

    @Autowired
    private UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.borrowing:borrowing-events}", groupId = "user-service")
    public void handleBorrowingEvent(String event) {
        try {
            logger.info("Received event: {}", event);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(event);
            String eventType = jsonNode.get("eventType").asText();
            Long userId = jsonNode.get("userId").asLong();

            switch (eventType) {
                case "BORROWING_CREATED":
                    userService.updateBorrowingStatus(userId);
                    logger.info("User borrowing status updated for userId: {}", userId);
                    break;
                case "BORROWING_RETURNED":
                    userService.unBorrowBook(userId);
                    logger.info("User borrowing status updated after return for userId: {}", userId);
                    break;
                default:
                    logger.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            logger.error("Error processing borrowing event: {}", e.getMessage(), e);
        }
    }

    



}

