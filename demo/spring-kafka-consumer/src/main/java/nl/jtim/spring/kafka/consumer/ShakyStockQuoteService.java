package nl.jtim.spring.kafka.consumer;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import org.springframework.stereotype.Service;

@Service
public class ShakyStockQuoteService implements StockQuoteService {



    @Override
    public void handle(OmsStockQuote stockQuote) {
        if ("KABOOM".equalsIgnoreCase(stockQuote.getSymbol())) {
            throw new RuntimeException("Whoops something went wrong...");
        }
        System.out.println("Consumed Stock : {}" + stockQuote);
    }
}

