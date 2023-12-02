package nl.jtim.spring.kafka.consumer;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OrderType;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@RestController
public class TestControllers {
    private final Consumer<String, OmsStockQuote> mykafkaConsumer;

    public TestControllers(Consumer<String, OmsStockQuote> kafkaConsumer) {
        this.mykafkaConsumer = kafkaConsumer;
        mykafkaConsumer.subscribe(Collections.singletonList("stock-quotes"));
    }

    @GetMapping("/orders/{orderBook}")
    public ResponseEntity<Map<OrderType, List<PriceQuantity>>> getOrders(@PathVariable String orderBook) {
        Map<OrderType, List<PriceQuantity>> orderData = new HashMap<>();
        Duration consumptionDuration = Duration.ofMillis(10); // Define the duration for consumption (10 milliseconds in this case)
        long startTime = System.currentTimeMillis();

        while (Duration.ofMillis(System.currentTimeMillis() - startTime).compareTo(consumptionDuration) < 0) {
            ConsumerRecords<String, OmsStockQuote> records = mykafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, OmsStockQuote> record : records) {
                OmsStockQuote stockOrder = record.value();
                PriceQuantity priceQuantity = new PriceQuantity(Integer.parseInt(stockOrder.getTradeValue()), 6);

                // Update or create the list for OrderType.BUY in orderData
                orderData.computeIfAbsent(OrderType.BUY, k -> new ArrayList<>()).add(priceQuantity);
            }
            mykafkaConsumer.commitSync();
        }

        return new ResponseEntity<>(orderData, HttpStatus.OK);
    }

    @GetMapping("/order/{orderBook}")
    public ResponseEntity<List<OmsStockQuote>> get10Orders(@PathVariable String orderBook) {
        List<OmsStockQuote> lastTenOrders = new ArrayList<>();

        // Consume messages from the topic
        ConsumerRecords<String, OmsStockQuote> records = mykafkaConsumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, OmsStockQuote> record : records) {
            lastTenOrders.add(record.value());

            // Limit to last 10 messages
            if (lastTenOrders.size() >= 10) {
                break;
            }
        }

        return new ResponseEntity<>(lastTenOrders, HttpStatus.OK);
    }
}

