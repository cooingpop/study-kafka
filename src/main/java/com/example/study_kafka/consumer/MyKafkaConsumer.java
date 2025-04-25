package com.example.study_kafka.consumer;

import com.example.study_kafka.model.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyKafkaConsumer {

    @KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
    public void listen(MyMessage message, Acknowledgment ack) {
        try {
            System.out.println("✅ 수신된 메시지: " + message);
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("처리 중 오류 발생", e);
        }
    }
}
