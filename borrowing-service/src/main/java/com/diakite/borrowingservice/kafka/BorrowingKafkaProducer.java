package com.diakite.borrowingservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BorrowingKafkaProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowingKafkaProducer.class);
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.borrowing:borrowing-events}")
    private String topic;

    public void sendBorrowingCreateEvent(Long borrowingId, Long userId, Long bookId) {
        String message = String.format("{\"eventType\": \"BORROWING_CREATED\", \"borrowingId\": %d, \"userId\": %d, \"bookId\": %d}", borrowingId, userId, bookId);
        kafkaTemplate.send(topic, message);
        logger.info("Borrowing create event sent: {}", message);
    }

    public void sendBorrowingReturnEvent(Long userId, Long bookId) {
        String message = String.format("{\"eventType\": \"BORROWING_RETURNED\", \"userId\": %d, \"bookId\": %d}", userId, bookId);
        kafkaTemplate.send(topic, message);
        logger.info("Borrowing return event sent: {}", message);
    }

    public void sendBorrowingDeleteEvent( Long bookId) {
        String message = String.format("{\"eventType\": \"BORROWING_DELETED\",\"bookId\": %d}", bookId);
        kafkaTemplate.send(topic, message);
        logger.info("Borrowing delete event sent: {}", message);
    }
} 