package nl.jtim.spring.kafka.producer.config;

import nl.jtim.spring.kafka.producer.ScheduledStockQuoteProducer;
import nl.jtim.spring.kafka.producer.StockQuoteProducer;
import nl.jtim.spring.kafka.producer.generator.RandomStockQuoteGenerator;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true")
    public ScheduledStockQuoteProducer scheduledStockQuoteProducer(StockQuoteProducer producer, RandomStockQuoteGenerator generator) {
        return new ScheduledStockQuoteProducer(producer, generator);
    }

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
}
