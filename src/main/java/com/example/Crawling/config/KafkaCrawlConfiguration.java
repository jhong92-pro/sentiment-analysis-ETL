package com.example.Crawling.config;

import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.KeywordRequest;
import com.example.Crawling.model.SentimentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.ListSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaCrawlConfiguration {
    @Bean
    public KafkaTemplate<String, CrawlRequest> kafkaSentimentPreStageListIdTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    private ProducerFactory<String, CrawlRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,10*1000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,10*1000);

        return props;

    }

    @Bean
    public KafkaTemplate<String, SentimentRequest> kafkaSentenceListByDateTemplate(){
        return new KafkaTemplate<>(sentimentProducerFactory());
    }

    private ProducerFactory<String, SentimentRequest> sentimentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(sentimentProducerProps());
    }

    private Map<String, Object> sentimentProducerProps() {
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,10*1000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,10*1000);

        return props;

    }

    // 안씀
    private Deserializer<List<Integer>> kafkaDeserializer() {
        ObjectMapper om = new ObjectMapper();
        om.getTypeFactory().constructParametricType(List.class, Integer.class);
        return new JsonDeserializer<>(om);
    }
}
