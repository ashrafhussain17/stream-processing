package nl.jtim.spring.kafka.producer;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.AddOrderMessageObject;
import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockQuoteProducer {

    private final KafkaTemplate<String, OmsStockQuote> kafkaTemplate;

    private final KafkaTemplate<String, AddOrderMessageObject> kafkaTemplate1;

    public StockQuoteProducer(KafkaTemplate<String, OmsStockQuote> kafkaTemplate, KafkaTemplate<String, AddOrderMessageObject> kafkaTemplate1) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
    }

    public void produce(OmsStockQuote stockQuote) {
        kafkaTemplate.send("stock-quotes", stockQuote.getSymbol(), stockQuote);
        log.info("Produced stock quote: {}", stockQuote);
    }

    public void produceAddOrder(AddOrderMessageObject addOrderMessageObject) {
        kafkaTemplate1.send("add-order-data-quote", addOrderMessageObject.getMessageType(), addOrderMessageObject);
        log.info("Produced stock quote: {}", addOrderMessageObject);
    }
}
