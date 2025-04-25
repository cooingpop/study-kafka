package com.example.study_kafka.controller;

import com.example.study_kafka.model.MyMessage;
import com.example.study_kafka.producer.KafkaMessageProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaMessageProducer producer;

    public KafkaController(KafkaMessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public ResponseEntity<String>  sendMessage(@RequestBody MyMessage message) {
        producer.send("my-topic", message);

        return ResponseEntity.ok("Message sent: " + message);
    }
}