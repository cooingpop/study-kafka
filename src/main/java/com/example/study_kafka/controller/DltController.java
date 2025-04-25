package com.example.study_kafka.controller;

import com.example.study_kafka.model.MyMessage;
import com.example.study_kafka.producer.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequiredArgsConstructor
public class DltController {

    private final KafkaMessageProducer producer;

    private final BlockingQueue<MyMessage> dltQueue = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "my-topic.DLT", groupId = "dlt-consumer")
    public void listenDlt(ConsumerRecord<String, MyMessage> record) {
        dltQueue.add(record.value());
    }

    @PostMapping("/dlt/retry")
    public String retryDltMessages() {
        int count = 0;
        while (!dltQueue.isEmpty()) {
            MyMessage message = dltQueue.poll();
            if (message != null) {
                producer.send("my-topic", message);
                count++;
            }
        }
        return "재처리된 메시지 수: " + count;
    }
}
