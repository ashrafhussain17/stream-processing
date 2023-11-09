package com.ashraf.kafkastreamconsumer;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaStreamsService {

    @KafkaListener(topics = "my-topic")
    public void consume(String message) {
        System.out.println("Received: " + message);
        // Process the message as needed
    }
//
//    public KafkaStreamsService(KafkaStreamsConfiguration configuration) {
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        KStream<String, YourInputDataClass> inputDataStream = builder.stream(
//                "my-topic",
//                Consumed.with(Serdes.String(), new JsonSerde<>(YourInputDataClass.class))
//        );
//
//        // Perform your processing on the input data stream here
//        // Example: inputDataStream.mapValues(value -> performSomeTransformation(value))
//
//        inputDataStream.to("output-topic", Produced.with(Serdes.String(), new JsonSerde<>(YourOutputDataClass.class)));
//
//        Properties streamsConfig = configuration.asProperties();
//        streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-kafka-streams-app");
//
//        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(streamsConfig));
//        kafkaStreams.start();
//    }
}