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
    // This class is not used, 관리자 기능 만들 때 필요
    
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

}
