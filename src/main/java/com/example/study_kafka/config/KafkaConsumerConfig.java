package com.example.study_kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offsetReset;

    @Value("${spring.kafka.consumer.trusted-packages}")
    private String trustedPackages;

    @Value("${spring.kafka.consumer.retry.delay-ms}")
    private long retryDelayMs;

    @Value("${spring.kafka.consumer.retry.max-attempts}")
    private long retryMaxAttempts;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();

        // 보안 상 허용할 패키지 제한 (실제 서비스에서는 특정 패키지 명시)
        deserializer.addTrustedPackages(trustedPackages);

        // 메시지 key 도 타입 매핑 (String or Json key 구분)
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers); // Kafka 브로커 주소
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);                   // 동일 그룹끼리 메시지 분산 소비
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);     // 오프셋 초기화 기준

        // 역직렬화 방식 설정
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        // 실패한 메시지를 Dead Letter Topic으로 이동시키는 recoverer 설정
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        // 고정된 재시도 정책 설정 (1000ms 간격으로 최대 3회 재시도)
        FixedBackOff fixedBackOff = new FixedBackOff(retryDelayMs, retryMaxAttempts);

        // 에러 핸들러에 재시도 정책 및 recoverer 설정
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);

        // 실패 로그 출력용 리스너 등록
        errorHandler.setRetryListeners((record, ex, attempt) -> {
            System.out.printf("메시지 처리 실패 (재시도 %d회차): %s%n", attempt, ex.getMessage());
        });

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
