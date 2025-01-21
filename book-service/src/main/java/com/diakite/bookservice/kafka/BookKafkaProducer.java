package com.diakite.bookservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(BookKafkaProducer.class);
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.book:book-events}")
    private String topic;

    public BookKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookDeleteEvent(Long bookId) {
        String message = String.format("{\"eventType\": \"BOOK_DELETED\", \"bookId\": %d}", bookId);
        kafkaTemplate.send(topic, message);
        logger.info("Book delete event sent: {}", message);
    }



} 