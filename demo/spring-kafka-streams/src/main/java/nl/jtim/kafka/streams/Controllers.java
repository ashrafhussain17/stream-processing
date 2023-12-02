package nl.jtim.kafka.streams;

import nl.jtim.spring.kafka.avro.stock.quote.StockData;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controllers {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public Controllers(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @GetMapping("test")
    public Long test() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        assert kafkaStreams != null;
        ReadOnlyKeyValueStore<String, Long> myStore = kafkaStreams
                .store(StoreQueryParameters.fromNameAndType("MyStore", QueryableStoreTypes.keyValueStore()));
        return myStore.get("GOOGL");
    }

    @GetMapping("/test1")
    public StockDataDTO getStockData(@RequestParam String stockCode) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        assert kafkaStreams != null;

        // Obtain a handle to the store
        ReadOnlyKeyValueStore<String, StockData> myStore = kafkaStreams
                .store(StoreQueryParameters.fromNameAndType("MarketDepthStore", QueryableStoreTypes.keyValueStore()));

        // Retrieve data from the store
        StockData stockData = myStore.get(stockCode);
        return convertToDTO(stockData);
    }

    // Method to convert Avro-generated StockData to DTO
    private StockDataDTO convertToDTO(StockData stockData) {
        StockDataDTO stockDataDTO = new StockDataDTO();
        stockDataDTO.setStockCode(stockData.getStockCode());

        StockInfoDTO stockInfoDTO = new StockInfoDTO();
        List<BuyDataDTO> buyDataDTOList = new ArrayList<>();
        stockData.getData().getBUY().forEach(buyData -> {
            buyDataDTOList.add(new BuyDataDTO(buyData.getPrice(), buyData.getQuantity()));
        });
        stockInfoDTO.setBUY(buyDataDTOList);
        List<SellDataDTO> sellDataDTOList = new ArrayList<>();
        stockData.getData().getSELL().forEach(buyData -> {
            sellDataDTOList.add(new SellDataDTO(buyData.getPrice(), buyData.getQuantity()));
        });
        stockInfoDTO.setSELL(sellDataDTOList);
        stockDataDTO.setData(stockInfoDTO);
        return stockDataDTO;
    }
}
