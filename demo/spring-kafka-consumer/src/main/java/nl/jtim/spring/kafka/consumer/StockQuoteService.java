package nl.jtim.spring.kafka.consumer;


import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;

public interface StockQuoteService {


    void handle(OmsStockQuote stockQuote);
}
