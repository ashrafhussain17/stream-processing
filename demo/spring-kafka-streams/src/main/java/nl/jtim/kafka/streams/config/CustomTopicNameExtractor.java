package nl.jtim.kafka.streams.config;

import com.dohatec.oms.dohaseclibrary.dto.avra.stock.AddOrderMessageObject;
import com.dohatec.oms.dohaseclibrary.dto.avra.stock.OmsStockQuote;
import org.apache.kafka.streams.processor.RecordContext;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTopicNameExtractor<T> implements TopicNameExtractor<String, T> {
    @Override
    public String extract(String key, T dto, RecordContext recordContext) {
        // Implement your logic here to extract topic names based on the DTO type
        // For instance, you might switch over different DTO types and extract topic names accordingly
        if (dto instanceof OmsStockQuote) {
            // Logic to extract topic name for YourFirstDTO
            return key;
        } else if (dto instanceof AddOrderMessageObject) {
            // Logic to extract topic name for YourSecondDTO
            return "topic_for_second_dto";
        }
        // Handle other cases or throw an exception for unknown DTO types
        throw new IllegalArgumentException("Unsupported DTO type");
    }
}