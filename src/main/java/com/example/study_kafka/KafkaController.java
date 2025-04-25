package com.example.study_kafka;

import com.example.study_kafka.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService producer;

    public KafkaController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public ResponseEntity<String>  sendMessage(@RequestBody MyMessage message) {
        producer.sendMessage("my-topic", message);

        return ResponseEntity.ok("Message sent: " + message);
    }
}