package com.example.Crawling.dao;

import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.entity.SentimentPreStage;
import com.example.Crawling.model.SentimentCode;
import com.example.Crawling.service.DomainCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentPreStageDao extends JpaRepository<SentimentPreStage, Integer> {
    List<SentimentPreStage> findBySentimentPreStageIdIn(List<Integer> sentimentPreStageId);
    Optional<SentimentPreStage> findByKeywordIdAndSentimentCodeAndCrawlResult(Integer keywordId, SentimentCode sentimentCode, CrawlResult crawlResult);
}

