package com.example.Crawling.entity;

import com.example.Crawling.model.SentimentCode;
import com.example.Crawling.service.CategoryCode;
import com.example.Crawling.service.DomainCode;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="sentiment_pre_stage")
@Builder
@ToString
public class SentimentPreStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sentimentPreStageId;
    @ManyToOne
    @JoinColumn(name = "crawl_result_id")
    private CrawlResult crawlResult;

    private int total;
    private int keywordId;
    private boolean isFinished;

    @Enumerated(value=EnumType.STRING)
    private SentimentCode sentimentCode;


}