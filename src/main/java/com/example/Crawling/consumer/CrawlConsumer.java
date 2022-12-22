package com.example.Crawling.consumer;

import com.example.Crawling.model.CrawlRequest;
import com.example.Crawling.model.KeywordRequest;
import com.example.Crawling.model.SentimentRequest;
import com.example.Crawling.service.IlbeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlConsumer {
    private final IlbeService ilbeService;
    @KafkaListener(id="keyword-id", topics="ilbe", containerFactory = "triggerRequestContainerFactory")
    public void triggerIlbe(KeywordRequest keywordRequest) throws Exception {
        ilbeService.sendSentimentAnalysisIteration(keywordRequest);
    }

    @KafkaListener(id="crawl-id", topics="crawl", containerFactory = "triggerCrawlContainerFactory")
    public void triggerChartListener(CrawlRequest sentimentPreStageIdList) throws Exception {
        ilbeService.crawlIlbe(sentimentPreStageIdList.getSentimentPreStageIdList());
    }

    @KafkaListener(id="sentiment-id", topics="sentiment", containerFactory = "triggerSentimentContainerFactory")
    public void triggerSentimentListener(SentimentRequest sentenceListByDate) throws Exception {
        for(Integer k:sentenceListByDate.getSentenceListById().keySet()){
            List<String> sentenceList = sentenceListByDate.getSentenceListById().get(k);
            log.info(Arrays.toString(sentenceList.toArray()));
        }
        ilbeService.requestSentiment(sentenceListByDate.getSentenceListById());
//        ilbeService.crawlIlbe(sentimentPreStageIdList.getSentimentPreStageIdList());
    }
}
