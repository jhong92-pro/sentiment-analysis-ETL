package com.example.Crawling.config;

import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.KeywordRequest;
import com.example.Crawling.model.SentimentRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.ListDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaJsonListenerContainerConfiguration {

    // KeywordRequest Configuration
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, KeywordRequest>> triggerRequestContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, KeywordRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(keywordConsumerFactory());
        return factory;
    }

    private ConsumerFactory<String,KeywordRequest> keywordConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                requestProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(KeywordRequest.class, false));
    }

    private Map<String, Object> requestProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    // KeywordRequest Configuration
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CrawlRequest>> triggerCrawlContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, CrawlRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(crawlConsumerFactory());
        return factory;
    }

    private ConsumerFactory<String, CrawlRequest> crawlConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                crawlProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(CrawlRequest.class, false));
    }

    private Map<String, Object> crawlProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    // SentimentKafkaRequest Configuration
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SentimentRequest>> triggerSentimentContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, SentimentRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sentimentConsumerFactory());
        return factory;
    }

    private ConsumerFactory<String, SentimentRequest> sentimentConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                sentimentProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(SentimentRequest.class, false));
    }

    private Map<String, Object> sentimentProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }
}
