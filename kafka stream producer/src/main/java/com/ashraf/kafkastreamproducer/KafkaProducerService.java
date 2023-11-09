package com.ashraf.kafkastreamproducer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 10000) // Send data every 10 second
    public void send() {
        String message = "Hello, Kafka! " + System.currentTimeMillis();
        kafkaTemplate.send(topic, message);
        System.out.println("Sent: " + message);
    }
}