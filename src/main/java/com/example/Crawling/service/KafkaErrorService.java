package com.example.Crawling.service;

import com.example.Crawling.dao.*;
import com.example.Crawling.entity.SentimentPreStageKafkaError;
import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.YnCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaErrorService {
    public final SentimentPreStageKafkaErrorDao sentimentPreStageKafkaErrorDao;



    @Transactional
    public void saveError(ConsumerRecord record) throws RuntimeException, ParseException {
        CrawlRequest crawlRequest = (CrawlRequest) record.value();
        List<SentimentPreStageKafkaError> sentimentPreStageKafkaErrors = new ArrayList<>();
        crawlRequest.getSentimentPreStageIdList()
                .forEach(
                        sentimentPreStageId->{
                            SentimentPreStageKafkaError sentimentPreStageError
                                    = SentimentPreStageKafkaError.builder()
                                    .sentimentPreStageId(sentimentPreStageId)
                                    .completeYn(YnCode.N)
                                    .build();
                            sentimentPreStageKafkaErrors.add(sentimentPreStageError);
                        }
                );
        sentimentPreStageKafkaErrorDao.saveAll(sentimentPreStageKafkaErrors);
        }

}
