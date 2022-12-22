package com.example.Crawling.producer;

import com.example.Crawling.model.CrawlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlProducer {
    private final KafkaTemplate<String, CrawlRequest> kafkaSentimentPreStageListIdTemplate;
    public void async(String topic, CrawlRequest sentimentPreStageIdList) throws RuntimeException {
        kafkaSentimentPreStageListIdTemplate.send(topic, sentimentPreStageIdList);
    }
}
