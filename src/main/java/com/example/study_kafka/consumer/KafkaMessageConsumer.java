package com.example.study_kafka.consumer;

import com.example.study_kafka.dto.MyMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageConsumer {

    @KafkaListener(
            topics = "my-topic",
            groupId = "json-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(MyMessage message) {
        System.out.println("수신한 메시지: " + message.getName());
    }
}