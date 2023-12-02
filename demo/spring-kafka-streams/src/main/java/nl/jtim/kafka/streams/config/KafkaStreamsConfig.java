package nl.jtim.kafka.streams.config;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import nl.jtim.spring.kafka.avro.stock.quote.BuyData;
import nl.jtim.spring.kafka.avro.stock.quote.SellData;
import nl.jtim.spring.kafka.avro.stock.quote.StockData;
import nl.jtim.spring.kafka.avro.stock.quote.StockInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.KafkaStreamBrancher;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static nl.jtim.kafka.streams.config.KafkaTopicsConfiguration.STOCK_QUOTES_TOPIC_NAME;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig<T> {

    private final CustomTopicNameExtractor<OmsStockQuote> topicNameExtractor;

    public KafkaStreamsConfig(CustomTopicNameExtractor<OmsStockQuote> topicNameExtractor) {
        this.topicNameExtractor = topicNameExtractor;
    }


    @Bean
    public KStream<String, OmsStockQuote> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, OmsStockQuote> stream = streamsBuilder.stream(STOCK_QUOTES_TOPIC_NAME);

//        KTable<String, StockData> myStore = stream.groupByKey()
//                .aggregate(
//                        () -> null, // initial value
//                        (key, value, aggregate) -> createStockDataFromOmsStockQuote(value),
//                        Materialized.<String, StockData, KeyValueStore<Bytes, byte[]>>as("MyNewStore")
//                                .withKeySerde(Serdes.String())
//                                .withValueSerde(getStockDataSerde())
//                );

        KTable<String, StockData> marketDepthStore = stream.groupByKey()
                .aggregate(
                        this::initializeStockData(), // initial value
                        (key, value, aggregate) -> updateMarketDepth(aggregate, value), // aggregator
                        Materialized.<String, StockData, KeyValueStore<Bytes, byte[]>>as("MarketDepthStore")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(getStockDataSerde())
                );

        return new KafkaStreamBrancher<String, OmsStockQuote>()
                .branch((key, value) -> value.getExchange().equalsIgnoreCase("NYSE"), kStream -> kStream.to(topicNameExtractor))
                .branch((key, value) -> value.getExchange().equalsIgnoreCase("NASDAQ"), kStream -> kStream.to(topicNameExtractor))
                .branch((key, value) -> value.getExchange().equalsIgnoreCase("AMS"), kStream -> kStream.to(topicNameExtractor))
                .defaultBranch(kStream -> kStream.to(topicNameExtractor))
                .onTopOf(stream);
    }



    private StockData updateMarketDepth(StockData existingData, OmsStockQuote newData) {
        StockInfo stockInfo = existingData.getData();
        String orderBook = newData.getSymbol();
        double newPrice = Double.parseDouble(newData.getTradeValue());
        int newQuantity = 10;

        // Check if the price already exists in BUY orders
        List<BuyData> buyOrders = stockInfo.getBUY();
        boolean priceExists = false;

        for (BuyData buyOrder : buyOrders) {
            if (buyOrder.getPrice() == newPrice) {
                buyOrder.setQuantity(buyOrder.getQuantity() + newQuantity);
                priceExists = true;
                break;
            }
        }

        // If the price doesn't exist, create a new entry
        if (!priceExists) {
            BuyData newBuyOrder = new BuyData(newPrice, newQuantity);
            buyOrders.add(newBuyOrder);
        }

        // Update the existing StockData with the modified StockInfo
        return StockData.newBuilder()
                .setStockCode(orderBook)
                .setData(stockInfo)
                .build();
    }

    // Method to create StockData from OmsStockQuote
    private StockData createStockDataFromOmsStockQuote(OmsStockQuote omsStockQuote) {
        String orderBook = omsStockQuote.getSymbol();
        int i = 0;
        double initialPrice = 00.0;
        BuyData buyData = new BuyData(Double.parseDouble(omsStockQuote.getTradeValue()), i);
        SellData sellData = new SellData(initialPrice + i, 6);

        i = i + 1;

        StockInfo stockInfo = StockInfo.newBuilder()
                .setBUY(List.of(buyData))
                .setSELL(List.of(sellData))
                .build();

        return StockData.newBuilder()
                .setStockCode(orderBook)
                .setData(stockInfo)
                .build();
    }

    // Method to get the Serde for StockData
    private SpecificAvroSerde<StockData> getStockDataSerde() {
        Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        SpecificAvroSerde<StockData> specificAvroSerde = new SpecificAvroSerde<>();
        specificAvroSerde.configure(serdeConfig, false);
        return specificAvroSerde;
    }
}
