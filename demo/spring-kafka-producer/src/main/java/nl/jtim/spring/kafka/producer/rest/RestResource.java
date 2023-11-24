package nl.jtim.spring.kafka.producer.rest;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import nl.jtim.spring.kafka.producer.StockQuoteProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/quotes")
public class RestResource {

    private final StockQuoteProducer stockQuoteProducer;

    public RestResource(StockQuoteProducer stockQuoteProducer) {
        this.stockQuoteProducer = stockQuoteProducer;
    }

    @PostMapping
    public void produce(@RequestBody StockQuoteRequest request) {
        OmsStockQuote stockQuote = new OmsStockQuote(request.getSymbol(), request.getExchange(), request.getTradeValue(), request.getCurrency(), request.getDescription(), Instant.now());
        stockQuoteProducer.produce(stockQuote);
    }
}
