package com.example.study_kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    /**
     * Kafka Producer 설정 구성
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 주소
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class); // 키 직렬화
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class); // JSON 직렬화
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // 모든 replica에 기록되면 ack
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // 재시도 횟수

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate 생성 - 실제 메시지 전송 시 사용하는 빈
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}