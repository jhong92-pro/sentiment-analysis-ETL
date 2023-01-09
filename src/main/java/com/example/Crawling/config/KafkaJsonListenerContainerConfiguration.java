package com.example.Crawling.config;

import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.KeywordRequest;
import com.example.Crawling.model.SentimentRequest;
import com.example.Crawling.service.IlbeService;
import com.example.Crawling.service.KafkaErrorService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.util.backoff.FixedBackOff;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.Option;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class KafkaJsonListenerContainerConfiguration {
    private final KafkaErrorService kafkaErrorService;

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
        factory.setRetryTemplate(customizedRetryTemplate());
        factory.setRecoveryCallback(context -> {
            ConsumerRecord record = (ConsumerRecord) context.getAttribute("record");
            System.out.println("consumermessage : "+record.value());
            kafkaErrorService.saveError(record);
            return Optional.empty();
        });
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
//        factory.setRetryTemplate(customizedRetryTemplate());
//        factory.setRecoveryCallback(context -> {
//            ConsumerRecord record = (ConsumerRecord) context.getAttribute("record");
//            System.out.println("consumermessage : "+record.value());
//            return Optional.empty();
//        });
        return factory;
    }

//    @Bean
//    DefaultErrorHandler eh() {
//        return new DefaultErrorHandler((rec, ex) -> {
//            System.out.println("Recovered: " + rec);
//        }, new FixedBackOff(1_000L, 3L));
//    }

    private RetryTemplate customizedRetryTemplate() {
        return new RetryTemplateBuilder()
                .fixedBackoff(1_000)
                .customPolicy(retryPolicy())
                .build();
    }

    private RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
        exceptions.put(Exception.class, true);
        return new SimpleRetryPolicy(3,exceptions);
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
