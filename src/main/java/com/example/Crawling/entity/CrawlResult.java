package com.example.Crawling.entity;

import com.example.Crawling.service.CategoryCode;
import com.example.Crawling.service.DomainCode;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="crawl_result")
@Builder
@ToString
public class CrawlResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int crawlResultId;
    private String title;
    private String url;
    private String body;
    private String ymd;

    @Enumerated(value=EnumType.STRING)
    private DomainCode domain;

    @Enumerated(value=EnumType.STRING)
    private CategoryCode category;
}