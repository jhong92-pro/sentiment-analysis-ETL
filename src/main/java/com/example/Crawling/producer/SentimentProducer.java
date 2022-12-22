package com.example.Crawling.producer;

import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.SentimentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SentimentProducer {
    private final KafkaTemplate<String, SentimentRequest> kafkaSentenceListByDateTemplate;
    public void async(String topic, SentimentRequest sentenceListByDate) throws RuntimeException {
        kafkaSentenceListByDateTemplate.send(topic, sentenceListByDate);
    }
}
