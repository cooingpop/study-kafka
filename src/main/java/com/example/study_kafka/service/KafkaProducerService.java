package com.example.study_kafka.service;

import com.example.study_kafka.MyMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, MyMessage message) {
        kafkaTemplate.send(topic, message);
        System.out.println("전송한 메시지: " + message.getName());
    }
}