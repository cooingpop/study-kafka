package com.example.study_kafka.consumer;

import com.example.study_kafka.dto.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaMessageConsumer {

    @KafkaListener(
            topics = "my-topic",
            groupId = "json-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(MyMessage message) {
        log.info("수신한 메시지: {}", message.getName());
    }
}