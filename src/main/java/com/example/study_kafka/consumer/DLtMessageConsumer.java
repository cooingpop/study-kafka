package com.example.study_kafka.consumer;

import com.example.study_kafka.dto.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DLtMessageConsumer {

    @KafkaListener(topics = "my-topic.DLT", groupId = "dlt-handler")
    public void handleDlt(MyMessage failedMessage) {
        // 예: 로그만 출력하거나 DB에 저장하거나
        log.error("DLT 처리 메시지: {}", failedMessage);
    }

}
