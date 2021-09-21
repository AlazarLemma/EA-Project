package com.example.jwt.integration;
import com.example.jwt.domain.UserRegisteredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserRegistered(String topic, UserRegisteredEvent message){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String resultAsString = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, resultAsString);
        } catch(Exception ex) {}
    }

    public void logToKafka(String message) {
        kafkaTemplate.send("appointment-log", message);
    }
}