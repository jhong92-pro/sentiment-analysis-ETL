package com.example.Crawling.controller;

import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.model.KeywordRequest;
import com.example.Crawling.model.SentimentResponse;
import com.example.Crawling.service.IlbeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrawlController {
    private final IlbeService ilbeService;

    @GetMapping("/keyword/{keywordId}")
    public List<CrawlResult> getKeyword(@PathVariable int keywordId) throws Exception {
        ilbeService.sendSentimentAnalysisIteration(KeywordRequest.builder()
                        .keywordId(keywordId)
                        .startDate("20000000")
                        .endDate("22222222")
                        .build());
        return null;
    }

    @GetMapping("/id/{id}")
    public List<CrawlResult> getId(@PathVariable int id) throws Exception {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        ilbeService.crawlIlbe(ids);
        return null;
    }

    @GetMapping("/sentiment")
    public void doSentiment() throws Exception {
        HashMap<Integer, List<String>> sentenceListByDate = new HashMap<>();
        List<String> sample1 = new ArrayList<>();
        sample1.add("안녕하세요");
        sample1.add("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요");
        sentenceListByDate.put(3, new ArrayList<>());
        List<String> sample2 = new ArrayList<>();
        sample2.add("안녕히 가세요");
        sample2.add("안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요");
        sentenceListByDate.put(4, new ArrayList<>());
        ilbeService.requestSentiment(sentenceListByDate);
    }

    @GetMapping("/sentiment/indiv")
    public SentimentResponse doSentimentIndiv() throws Exception {
        HashMap<String, List<String>> sentenceListByDate = new HashMap<>();
        List<String> sample1 = new ArrayList<>();
        sample1.add("안녕하세요");
        sample1.add("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요");
        sample1.add("안녕히 가세요");
        sample1.add("안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요안녕히 가세요");
        sample1.add("abcdefg");
        sentenceListByDate.put("sentences", new ArrayList<>());
        return ilbeService.requestSentimentById(sample1);
    }


}
