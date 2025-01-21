package com.diakite.userservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserKafkaProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.user:user-events}")
    private String topic;

    public UserKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserUpdateEvent(Long userId) {
        String message = String.format("{\"eventType\": \"USER_UPDATE\", \"userId\": %d}", userId);
        kafkaTemplate.send(topic, message);
        logger.info("User update event sent: {}", message);
    }

    public void sendUserDeleteEvent(Long userId) {
        String message = String.format("{\"eventType\": \"USER_DELETED\", \"userId\": %d}", userId);
        kafkaTemplate.send(topic, message);
        logger.info("User delete event sent: {}", message);
    }





} 