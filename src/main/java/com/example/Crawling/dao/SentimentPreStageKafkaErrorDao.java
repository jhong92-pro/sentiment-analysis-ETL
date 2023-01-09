package com.example.Crawling.dao;

import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.entity.SentimentPreStage;
import com.example.Crawling.entity.SentimentPreStageKafkaError;
import com.example.Crawling.model.SentimentCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentimentPreStageKafkaErrorDao extends JpaRepository<SentimentPreStageKafkaError, Integer> {

}

