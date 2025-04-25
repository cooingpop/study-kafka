package com.example.study_kafka.service;

import com.example.study_kafka.MyMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "my-topic",
            groupId = "json-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(MyMessage message) {
        System.out.println("📥 수신한 메시지: " + message.getName());
    }
}