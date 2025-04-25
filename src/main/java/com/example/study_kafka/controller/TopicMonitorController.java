package com.example.study_kafka.controller;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class TopicMonitorController {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @GetMapping("/topic/info")
    public Map<String, Object> topicInfo() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "monitor-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Map<String, Object> info = new HashMap<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
             AdminClient admin = AdminClient.create(props)) {

            Set<String> topics = consumer.listTopics().keySet();
            DescribeTopicsResult result = admin.describeTopics(topics);
            Map<String, TopicDescription> descMap = result.all().get();

            for (String topic : topics) {
                TopicDescription desc = descMap.get(topic);
                info.put(topic, Map.of(
                        "partitions", desc.partitions().size(),
                        "internal", desc.isInternal()
                ));
            }
        }

        return info;
    }
}
