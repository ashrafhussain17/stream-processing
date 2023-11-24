package nl.jtim.spring.kafka.producer;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.AddOrderMessageObject;
import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OrderType;
import lombok.extern.slf4j.Slf4j;
import nl.jtim.spring.kafka.producer.generator.RandomStockQuoteGenerator;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class ScheduledStockQuoteProducer {

    private final StockQuoteProducer producer;
    private final RandomStockQuoteGenerator generator;

    public ScheduledStockQuoteProducer(StockQuoteProducer producer, RandomStockQuoteGenerator generator) {
        this.producer = producer;
        this.generator = generator;
    }

    @Scheduled(fixedRateString = "${kafka.producer.rate}")
    public void produce() {
        OmsStockQuote stockQuote = generator.generate();
        producer.produce(stockQuote);
    }

//    @Scheduled(fixedRateString = "${kafka.producer.rate}")
//    public void addOrderDataProduce() {
//        AddOrderMessageObject sampleObject = new AddOrderMessageObject(
//                "B",              // messageType
//                123456,              // timestampNanoseconds
//                789L,                // orderNumber
//                OrderType.BUY,       // orderVerb
//                100L,                // quantity
//                456L,                // orderBook
//                50L                  // price
//        );
//        producer.produceAddOrder(sampleObject);
//    }
}
