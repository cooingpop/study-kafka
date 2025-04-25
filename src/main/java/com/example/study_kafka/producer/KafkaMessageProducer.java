package com.example.study_kafka.producer;

import com.example.study_kafka.model.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, MyMessage message) {
        kafkaTemplate.send(topic, message);
        log.info("전송한 메시지: {}", message.getName());
    }
}