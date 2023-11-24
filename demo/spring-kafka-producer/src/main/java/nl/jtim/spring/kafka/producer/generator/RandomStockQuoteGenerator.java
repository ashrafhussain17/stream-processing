package nl.jtim.spring.kafka.producer.generator;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;


@Component
public class RandomStockQuoteGenerator extends AbstractRandomStockQuoteGenerator {

    public OmsStockQuote generate() {
        Instrument randomInstrument = pickRandomInstrument();
        BigDecimal randomPrice = generateRandomPrice();
        return new OmsStockQuote(randomInstrument.getSymbol(), randomInstrument.getExchange(), randomPrice.toPlainString(),
                randomInstrument.getCurrency(), randomInstrument.getSymbol() + " stock", Instant.now());
    }
}
